package com.likelion.timer.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// /api/v1/Youtube/** 경로에 대한 CORS 설정
		registry.addMapping("/api/v1/Youtube/**")
			.allowedOrigins("http://localhost:8080")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true);

		// /api/** 경로에 대한 CORS 설정
		registry.addMapping("/api/**")
			.allowedOrigins("http://localhost:3000") // 다른 클라이언트의 도메인
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}