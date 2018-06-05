package com.nonobank.test.interceptors;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by H.W. on 2018/4/25.
 */
//@Component
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockInterceptor())
                .addPathPatterns("/web-mock/**")
                .excludePathPatterns("/web-mock/mock")
                .excludePathPatterns("/web-mock/distribute");
        super.addInterceptors(registry);
    }
}
