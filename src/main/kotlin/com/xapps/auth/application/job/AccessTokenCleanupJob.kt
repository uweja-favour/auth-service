package com.xapps.auth.application.job

import com.xapps.auth.persistence.repository.impl.AccessTokenRepository
import com.xapps.time.clock.ClockProvider
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AccessTokenCleanupJob(
    private val repository: AccessTokenRepository,
    private val clockProvider: ClockProvider
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 3 * * ?") // daily cleanup
    suspend fun cleanUpExpiredAccessTokens() {
        val cutoff = clockProvider.now()
        val deleted = repository.deleteExpiredOrRevokedBefore(cutoff)
        if (deleted > 0) {
            log.info("🧹 Cleaned up $deleted expired/revoked access tokens")
        }
    }
}