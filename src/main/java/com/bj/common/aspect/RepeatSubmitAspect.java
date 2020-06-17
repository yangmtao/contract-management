package com.bj.common.aspect;

import com.bj.common.annotation.NoRepeatSubmit;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.R;
import com.bj.common.util.RequestUtils;
import com.bj.sys.service.impl.RedisLock;
import com.bj.sys.shiro.ShiroUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * desc:防止表单重复提交
 * Redis 分布式锁实现
 * 如有疑问可参考@see<a href="https://www.cnblogs.com/linjiqin/p/8003838.html">Redis分布式锁的正确实现方式</a>
 * @author zhph
 * @date 2019/8/6  20:01
 */
@Aspect
@Component
public class RepeatSubmitAspect {

    @Autowired
    private RedisLock redisLock;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();

        HttpServletRequest request = RequestUtils.getRequest();

        // 此处可以用token或者JSessionId
        String token = ShiroUtils.getSession().getId().toString();

        String path = request.getServletPath();
        String key = getKey(token, path);
        String clientId = getClientId();
        boolean isSuccess = redisLock.tryLock(key, clientId, lockSeconds);

        if (isSuccess) {
            // 获取锁成功
            Object result;

            try {
                // 执行进程
                result = pjp.proceed();
            } finally {
                // 解锁
                redisLock.releaseLock(key, clientId);
            }

            return result;

        } else {
            // 获取锁失败，认为是重复提交的请求
            return R.error(CommonEnum.ReturnCode.ERROR.getValue(), "重复请求，请稍后再试");
        }

    }

    private String getKey(String token, String path) {
        return token + path;
    }

    private String getClientId() {
        return UUID.randomUUID().toString();
    }
}
