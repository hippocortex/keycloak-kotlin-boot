package com.xebia.keycloak.application.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    companion object {
        const val BEARER_SCHEME = "oauth2 bearer"
        const val OAUTH2_IMPLICIT = "implicit"

    }

    @Value("\${application.keycloak.url}/realms/microservices/protocol/openid-connect")
    private lateinit var keycloakUrl: String


    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("View skills")
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
                    ).addSecuritySchemes(
                        OAUTH2_IMPLICIT,
                        SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .flows(
                                OAuthFlows().implicit(
                                    OAuthFlow()
                                        .authorizationUrl("$keycloakUrl/auth")
                                        .tokenUrl("$keycloakUrl/token")
                                )
                            )
                    )
            )
    }
}