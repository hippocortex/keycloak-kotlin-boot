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

    companion object {
        private const val FAKE_CLOCK = "2022-01-20T09:10:39Z"
    }

    @Test
    fun `should get skills if we have token`() {
        //givenClockIsFixedAt(FAKE_CLOCK)

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
                [{"id":1,"skill":"Java","category":"BackEnd"},{"id":2,"skill":"Python","category":"BackEnd"}]
            """.trimIndent())
        } Extract {
            println(this.asString())
        }

    }

    @Test
    fun `should not get skills if we don't have a good token`() {
        //givenClockIsFixedAt(FAKE_CLOCK)

        Given {
            header(
                "Authorization",
                "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJzencwMUxuVlVBQWJxSnpWemp1N1JzbGJTcTRWS0huczRkX0N0Z3JUOHhBIn0.eyJleHAiOjE2NDY2NDg0NzgsImlhdCI6MTY0NjY0ODE3OCwianRpIjoiYTMwYzhjZDctMGE0Ny00Nzg0LWEzNTEtNjc0ZTE3MzQ5MjM4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5NTk1L2F1dGgvcmVhbG1zL2FwcHNwcmluZ29hdXRoIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQzNGVkMzA5LThiNzAtNDMxZi05NWM4LWY0ZjZkZDJkOTUyZCIsInR5cCI6IkJlYXJlciIsImF6cCI6InNwcmluZy1hcGkiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiYWRtaW4iLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjbGllbnRIb3N0IjoiMTcyLjIwLjAuMSIsImNsaWVudElkIjoic3ByaW5nLWFwaSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNwcmluZy1hcGkiLCJjbGllbnRBZGRyZXNzIjoiMTcyLjIwLjAuMSJ9.QmvbjXHrA8FHEB0qrpqGVQtDVpGimsOUcXUx4RCP27ARqt08_33F1WrI51dnAr5A9lS5tmdbkqEGnrL4LP87CR9Y5fqcx6GyMQbUUDFLG7QdI2v9Fv72bxH-hNN-ENef4MxBacL5gsnYvnt1nT-5FsdUEWoTgIpUNM4A4Z7Ku0-gT3c1pyZRG8qvlw6cZJNmluKMC_V3al3QbWmrDcgEJl5L05aWnoCjnlCAQQUUVke3nVxzEAWdJhYCppAscYfqoRA7qw7h7WylzkebUXflX2_WBaH6koxXl-xEg0aXnqoPpUL5rK0c9tGHu-teKtcGNSR8zuNiBmJQsHaiCA"
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