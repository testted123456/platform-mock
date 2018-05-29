package com.nonobank.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EntityScan(
		basePackageClasses = { WebMockApplication.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
@EnableRedisHttpSession
@EnableAutoConfiguration
public class WebMockApplication /*extends SpringBootServletInitializer*/ {

	public static void main(String[] args) {
		SpringApplication.run(WebMockApplication.class, args);
	}

	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		builder.sources(this.getClass());
		return super.configure(builder);
	}*/
}
