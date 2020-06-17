package com.bj.interceptor;

import com.bj.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

/**
 * desc:
 *
 * @author zhph
 * @date 2019/11/22  9:27
 */
public class TimeOutIntercepter implements HandlerInterceptor {
    Logger logger=LoggerFactory.getLogger(getClass());
    /**
     * 可以随意访问的url
     */
    public String[] allowUrls;

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession(true);
        String excludeUrl="/sys/login";
        Long operateTime ;
        if (StringUtils.isNoneBlank(requestUrl)) {
            for (String url : allowUrls) {
                if (requestUrl.contains(url)) {
                    return true;
                }
            }
        }
        //session持续时间
        int maxInactiveInterval = session.getMaxInactiveInterval();
        //session创建时间
        long creationTime = session.getCreationTime();
        //session最新链接时间
        long lastAccessedTime = session.getLastAccessedTime();

        logger.debug("-----> session持续时间: " + maxInactiveInterval);
        logger.debug("-----> session创建时间: " + DateUtil.format(new Date(creationTime),"yyyy-MM-dd HH:mm:ss"));
        logger.debug("-----> session最新链接时间: " + DateUtil.format(new Date(lastAccessedTime),"yyyy-MM-dd HH:mm:ss"));

        //登录后重新计算操作时间
        if(requestUrl.contains(excludeUrl)){
            operateTime = null;
        }else{
            //从session获取上次链接时间
            operateTime = (Long) session.getAttribute("operateTime");
        }

        //如果operateTime是空，说明是第一次链接，对operateTime进行初始化
        if (operateTime == null) {
            session.setAttribute("operateTime", lastAccessedTime);
            return true;
        } else {
            logger.debug("-----> 从session获取上次链接时间: " + DateUtil.format(new Date(operateTime),"yyyy-MM-dd HH:mm:ss"));
            //计算最新链接时间和上次链接时间的差值
            int intervalTime = (int) ((lastAccessedTime - operateTime) / 1000);
            logger.debug("-----> 操作间隔时间: " + intervalTime);
            int minTimeLength=3500;
            //如果超过十秒没有交互的话，就跳转到超时界面
            if (intervalTime > minTimeLength) {
                String requestType= request.getHeader("X-Requested-With");
                String xMLHttpRequest="XMLHttpRequest";
                if(xMLHttpRequest.equalsIgnoreCase(requestType)){
                    //异步请求，自定义回话超时错误码
                    response.setHeader("sessionStatus","timeout");
                    response.sendError(518,"session timeout.");
                }else{
                    String redirectUrl=request.getContextPath() + "/login.html";
                    PrintWriter out = response.getWriter();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<script type=\"text/javascript\" charset=\"utf-8\">");
                    stringBuilder.append("top.location.href=\""+redirectUrl+"\";");
                    stringBuilder.append("</script>");
                    out.print(stringBuilder.toString());
                    out.close();
                }
                return false;
            }
            //更新operateTime
            session.setAttribute("operateTime", lastAccessedTime);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}