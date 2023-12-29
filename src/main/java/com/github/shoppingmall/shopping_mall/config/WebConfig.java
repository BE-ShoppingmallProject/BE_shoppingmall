package com.github.shoppingmall.shopping_mall.config;

import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @org.springframework.beans.factory.annotation.Value("${post.file_path}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // 외부 디렉토리를 정적 리소스 경로로 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + filePath);
    }
}
