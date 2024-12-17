package com.ohgiraffers.chap05springdata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:53614") // flutter 실행 시 나오는 포트로 변경 or 포트포워딩
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");

    }
}
