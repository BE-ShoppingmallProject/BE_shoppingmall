package com.github.shoppingmall.shopping_mall.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerApi() {

        return new OpenAPI()
                .info(new Info().title("쇼핑몰 프로젝트 API 명세서").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("tokenScheme"))
                .components(new Components()
                        .addSecuritySchemes("tokenScheme", new SecurityScheme()
                                .name("token")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .bearerFormat("JWT")
                        )
                );
    }
}