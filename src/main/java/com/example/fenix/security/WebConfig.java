package com.example.fenix.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // toda vez que alguém acessar localhost:8080/upload/foto.jpg, o java vai buscar na pasta física 'uploads'
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:uploads/");
    }
}