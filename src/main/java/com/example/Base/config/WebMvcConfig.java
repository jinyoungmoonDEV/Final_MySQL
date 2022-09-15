package com.example.Base.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/")
						.allowedOrigins("http://13.209.99.47:3000") //React서버의 url:port
						.allowedOrigins("http://13.209.99.47:3001") //React서버의 url:port
						.allowedOrigins("http://13.209.99.47:3002") //React서버의 url:port
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true)
						.maxAge(MAX_AGE_SECS);
	}
}
