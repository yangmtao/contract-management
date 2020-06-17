package com.bj.config;

import com.bj.interceptor.TimeOutIntercepter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.0.0 2018-01-25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
        registry.addResourceHandler("/localImage/**").addResourceLocations("file:D:/image/");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/"+Constant.fileAddress);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TimeOutIntercepter timeOutIntercepter = new TimeOutIntercepter();
        String [] allowUrls = {"logout"};
        timeOutIntercepter.setAllowUrls(allowUrls);
        //添加拦截器
        registry.addInterceptor(timeOutIntercepter).addPathPatterns("/sys/**", "/business/**");
    }
}
