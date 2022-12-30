package com.alfonsoristorato.lucreziaspresentbackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfiguration {
    @Value("${web.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${web.cors.allowed-methods}")
    private String[] allowedMethod;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/entry").allowedMethods(allowedMethod).allowedOrigins(allowedOrigins);
            }
        };
    }
}
