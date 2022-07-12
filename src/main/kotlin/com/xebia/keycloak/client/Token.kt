package com.xebia.keycloak.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit

@JsonIgnoreProperties(ignoreUnknown = true)
data class Token(
        @JsonProperty("access_token")
        val accessToken: String,
        @JsonProperty("expires_in")
        val expiresIn: String,

        @JsonProperty("refresh_expires_in")
        val refreshExpiresIn : String
) {
        private val accessTokenExpirationTime = Instant.now().plus(expiresIn.toLong(), ChronoUnit.SECONDS)
        private val refreshTokenExpirationTime = Instant.now().plus(refreshExpiresIn.toLong(), ChronoUnit.SECONDS)

        /*
         * to avoid error because of time not properly sync we add a small amount of time
         */
        fun accessTokenIsExpiredOrAlmostExpired(clock: Clock = Clock.systemUTC()): Boolean {
                return accessTokenExpirationTime
                        .minus(2, ChronoUnit.SECONDS)
                        .isBefore(clock.instant())
        }

        fun refreshTokenIsExpired(clock: Clock = Clock.systemUTC()): Boolean {
                return refreshTokenExpirationTime.isBefore(clock.instant())
        }
}