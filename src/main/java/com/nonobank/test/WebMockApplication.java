package com.nonobank.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebMockApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WebMockApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		builder.sources(this.getClass());
		return super.configure(builder);
	}
}
