package com.xebia.keycloak.common

import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime

@Service
class TimeService(private val clock: Clock = Clock.systemUTC()) {
    fun getOffsetDateTimeNow(): OffsetDateTime = OffsetDateTime.now(clock)

    fun getInstantNow(): Instant = clock.instant()

    fun getDateToday(): LocalDate = LocalDate.now(clock)
}