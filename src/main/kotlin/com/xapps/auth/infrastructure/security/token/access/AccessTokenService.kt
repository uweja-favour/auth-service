package com.xapps.auth.infrastructure.security.token.access

import com.xapps.auth.dto.RawAccessToken
import com.xapps.auth.infrastructure.security.model.AccessToken
import com.xapps.auth.infrastructure.security.token.JtiFactory
import com.xapps.auth.persistence.repository.impl.AccessTokenRepository
import com.xapps.time.clock.ClockProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Service
class AccessTokenService(
    private val factory: AccessTokenFactory,
    private val repository: AccessTokenRepository,
    private val jtiFactory: JtiFactory,
    private val clockProvider: ClockProvider
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${jwt.access-token.expiration-ms:900000}") // 15 minutes default
    private val accessTokenDurationMillis = 15.minutes.inWholeMilliseconds

    suspend fun issueToken(
        userId: String,
        email: String,
        role: String,
        expiryMillis: Long = accessTokenDurationMillis
    ): RawAccessToken {

        repository.revokeAllActiveByUserId(userId)

        val jti = jtiFactory.createNewJti()

        val rawAccessToken = factory.create(
            userId = userId,
            jti = jti,
            email = email,
            role = role,
            expiryMillis = expiryMillis
        )

        repository.insert(
            AccessToken.new(
                jti = jti,
                userId = userId,
                expiryAt = tokenExpiry(expiryMillis),
                now = clockProvider.now()
            )
        )

        return rawAccessToken
    }

    suspend fun revokeAllTokensByUserId(userId: String) {
        repository.revokeAllActiveByUserId(userId)
    }

    private fun tokenExpiry(ms: Long) =
        clockProvider.now().plus(ms.milliseconds)
}