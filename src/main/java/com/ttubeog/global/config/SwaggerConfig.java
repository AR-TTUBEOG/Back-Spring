package com.ttubeog.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        License license = new License();
        license.setName("Ttubeog");

        Info info = new Info()
                .title("\"뚜벅 API문서\"")
                .description("뚜벅의 API 문서 입니다.")
                .version("v0.0.1")
                .license(license);;

        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt); // 헤더에 토큰 포함
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
