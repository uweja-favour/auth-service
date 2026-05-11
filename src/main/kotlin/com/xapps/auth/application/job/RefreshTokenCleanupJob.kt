package com.xapps.auth.application.job

import com.xapps.auth.persistence.repository.impl.RefreshTokenRepository
import com.xapps.time.clock.ClockProvider
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.time.toKotlinInstant

@Component
class RefreshTokenCleanupJob(
    private val repository: RefreshTokenRepository,
    private val clockProvider: ClockProvider
) {

    @Scheduled(cron = "0 0 3 * * ?") // every day at 3AM
    suspend fun cleanExpiredTokens() {
        val now = clockProvider.now()
        val deleted = repository.deleteExpiredAndRevoked(now)

        if (deleted > 0) {
            println("Cleaned $deleted expired/revoked tokens")
        }
    }
}