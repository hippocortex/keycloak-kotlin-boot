package com.xebia.keycloak.application

import com.xebia.keycloak.AbstractSpringIT
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort

class PublicEndpointIT : AbstractSpringIT() {

    @LocalManagementPort
    var managementPort = 0

    @Test
    fun `Should return 200 when calling health check without Bearer`() {
        Given {
            port(managementPort)
        } When {
            get("/actuator/health")
        } Then {
            statusCode(200)
        } Extract {
            println(this.asString())
        }
    }

    @Test
    fun `Should return 200 when calling open api url without Bearer`() {
        When {
            get("/skills/v1/api-docs")
        } Then {
            statusCode(200)
        } Extract {
            println(this.asString())
        }
    }
}