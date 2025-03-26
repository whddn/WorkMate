package com.workmate.app.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ /uploads/** 요청이 /home/user/uploads/ 실제 폴더와 매핑되도록 설정
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/workmate/"); // ✅ 실제 파일 경로

    }
}
