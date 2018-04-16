package com.nonobank.test.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by H.W. on 2018/4/12.
 */
@Component
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MockInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/mock");
        super.addInterceptors(registry);
    }
}