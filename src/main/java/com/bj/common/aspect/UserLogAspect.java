package com.bj.common.aspect;

import com.bj.common.annotation.UserLog;
import com.bj.common.util.HttpContextUtils;
import com.bj.common.util.IPUtils;
import com.bj.common.util.SnowflakeIdWorker;
import com.bj.sys.entity.SysUserEntity;
import com.bj.sys.entity.UserOperationEntity;
import com.bj.sys.service.BjUserOperationService;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 用户操作日志，切面处理类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.3.0 2017-03-08
 */
@Aspect
@Component
public class UserLogAspect {
	@Resource
	private BjUserOperationService operationService;
	
	@Pointcut("@annotation(com.bj.common.annotation.UserLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		//保存日志
		saveUserOperationLog(point, time);
		return result;
	}

	private void saveUserOperationLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		UserOperationEntity operationEntity = new UserOperationEntity();
		UserLog userOperationLog = method.getAnnotation(UserLog.class);

		if(userOperationLog != null){
			//注解上的描述
			operationEntity.setOpModule(userOperationLog.module());
			operationEntity.setOpDesc(userOperationLog.remark());
			operationEntity.setUsePlatform(userOperationLog.usePlatform());
		}


		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = new Gson().toJson(args[0]);
			operationEntity.setOpContent(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		operationEntity.setUserIp(IPUtils.getIpAddr(request));

		//客户账号
		Long userId=((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUserId();
		operationEntity.setUserId(userId);


		operationEntity.setOpDate(new Date());
		operationEntity.setOpId(SnowflakeIdWorker.getInstance().nextId());
		operationEntity.setOpSource(Integer.valueOf("2"));
		//保存系统日志
		operationService.insert(operationEntity);
	}
}
