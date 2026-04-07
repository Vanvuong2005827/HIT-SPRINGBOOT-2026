package com.hit.springboot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HIT Spring Boot API")
                        .version("1.0.0")
                        .description("API cho ứng dụng Spring Boot — HIT Club — Buổi 5"));
    }
}
