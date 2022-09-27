package com.xebia.keycloak.application.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    companion object {
        const val BEARER_SCHEME = "oauth2 bearer"
    }

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Paylib payment")
                    .version("1.3.0")
            ).components(
                Components()
                    .addSecuritySchemes(
                        BEARER_SCHEME,
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("Access token provided by authentication server")
                    )
            )
    }
}