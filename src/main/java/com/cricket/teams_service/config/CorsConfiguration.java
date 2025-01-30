package com.cricket.teams_service.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfiguration implements WebMvcConfigurer{
	 
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
	        
		registry.addMapping("/**")
	    .allowedOrigins("http://localhost:5173") // Your React frontend URL
	    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	    .allowedHeaders("*")
	    .allowCredentials(true);

}
}