package com.cos.jwt.config;

import com.cos.jwt.fllter.MyFIlter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFIlter> filter(){
        FilterRegistrationBean<MyFIlter> bean =new FilterRegistrationBean<>(new MyFIlter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }
}
