package com.crazy.config.web;

import com.crazy.web.handler.interceptor.CrossInterceptorHandler;
import com.crazy.web.interceptor.UserInterceptorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class UKWebAppConfigurer 
        extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new UserInterceptorHandler()).addPathPatterns("/**")
                .excludePathPatterns(
                        "/login.html",
                        "/api/**",
                        "/tokens",
                        "/registerPlayer/**",
                        "/presentapp/**",
                        "/wxController/**"
                );
    	registry.addInterceptor(new CrossInterceptorHandler()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}