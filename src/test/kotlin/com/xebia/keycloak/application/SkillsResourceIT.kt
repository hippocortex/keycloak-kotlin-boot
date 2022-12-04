package com.xebia.keycloak.application

import com.xebia.keycloak.AbstractSpringIT
import com.xebia.keycloak.utils.assertJsonBodyIsEqualStrict
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test

class SkillsResourceIT : AbstractSpringIT() {

    @Test
    fun `should get skills if we have token`() {
        Given {
            header(
                "Authorization",
                "Bearer ${generateJWT(true)}"
            )
            contentType(ContentType.JSON)
        } When {
            get("/skills/v1/public/view")
        } Then {
            statusCode(200)
            assertJsonBodyIsEqualStrict("""
                [{"id":1,"type":"Java","category":"BackEnd"},{"id":2,"type":"Python","category":"BackEnd"}]
            """.trimIndent())
        } Extract {
            println(this.asString())
        }

    }

    @Test
    fun `should not get skills if we don't have a good token`() {
        Given {
            header(
                "Authorization",
                "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJzencwMUxuVlVBQWJxSnpWemp1N1JzbGJTcTRWS0huczRkX0N0Z3JUOHhBIn0.eyJleHAiOjE2NDY2Njc0NzYsImlhdCI6MTY0NjY2NzE3NiwianRpIjoiMjcwNWVkYzEtMWVjZS00N2I4LThlNWUtODgwNjcyNTM4YzAyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5NTk1L2F1dGgvcmVhbG1zL2FwcHNwcmluZ29hdXRoIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQzNGVkMzA5LThiNzAtNDMxZi05NWM4LWY0ZjZkZDJkOTUyZCIsInR5cCI6IkJlYXJlciIsImF6cCI6InNwcmluZy1hcGkiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiYWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjbGllbnRIb3N0IjoiMTcyLjIwLjAuMSIsImNsaWVudElkIjoic3ByaW5nLWFwaSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNwcmluZy1hcGkiLCJjbGllbnRBZGRyZXNzIjoiMTcyLjIwLjAuMSJ9.x5a3VpYxUGSavsJmGB3yk02EhTWHGgXw7fWkuuj3hD2tqLeCJBbIWPemIlIYnY7tyKko5b-yd4CL8rAJYDk-U_wJ5rIauFb2OoY8wgKJZgQjulblvwjYOQFadDPqOV-xZcDnctJV8F41rUBetDBmqWyCQtAMsUjnLJqNWNR6nEofErNwVv9kFFONbUSYZGa3fo4ZFgb_pl3rZBSwey-ADYOssJT6SLOulg4MW3shBRxv-BLnquX-N4Dt6_mInWfZJQXqWQfoL2K00YOK8GdD9C91Sc5elbXj5PDwGINerTZ67NIMI5fRWIfuimdkvnKuUrO3q1FF6U7p5IvN_Qo2cg"
            )
            contentType(ContentType.JSON)
        } When {
            get("/skills/v1/public/view")
        } Then {
            statusCode(401)

        } Extract {
            println(this.asString())
        }
    }
}