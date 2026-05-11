package com.xapps.auth.infrastructure.security.token.refresh

import com.xapps.auth.domain.exceptions.RefreshTokenInvalidException
import com.xapps.auth.dto.RawRefreshToken
import com.xapps.auth.infrastructure.security.jwt.JwtKeys
import com.xapps.auth.infrastructure.security.model.RefreshToken
import com.xapps.auth.infrastructure.security.model.ensureNotExpired
import com.xapps.auth.infrastructure.security.model.ensureNotRevoked
import com.xapps.auth.infrastructure.security.token.JtiFactory
import com.xapps.auth.persistence.repository.impl.RefreshTokenRepository
import com.xapps.time.clock.ClockProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

@Service
class RefreshTokenService(
    private val repository: RefreshTokenRepository,
    private val jtiFactory: JtiFactory,
    private val factory: RefreshTokenFactory,
    private val hasher: RefreshTokenHasher,
    private val jwtKeys: JwtKeys,
    private val clockProvider: ClockProvider
) {

    @Value("\${jwt.refresh-token.expiration-ms:2592000000}") // 30 days default
    private var refreshTokenDurationMillis: Long = 30.days.inWholeMilliseconds

    suspend fun issueToken(
        userId: String
    ): RawRefreshToken {

        val expiryMillis: Long = refreshTokenDurationMillis

        repository.revokeAllActiveByUserId(userId)

        val jti = jtiFactory.createNewJti()
        val rawRefreshToken = factory.create(
            userId = userId,
            jti = jti,
            expiryMillis = expiryMillis
        )
        val hash = hasher.hash(rawRefreshToken.value, jwtKeys.refreshSecretKey)

        val tokenRecord = RefreshToken(
            jti = jti,
            userId = userId,
            tokenHash = hash,
            createdAt = clockProvider.now(),
            expiryAt = clockProvider.now() + (expiryMillis.milliseconds),
            lastUsedAt = null,
            revoked = false
        )

        repository.insert(tokenRecord)

        return rawRefreshToken
    }

    suspend fun verify(refreshToken: RawRefreshToken): RefreshToken {
        val hash = hasher.hash(refreshToken.value, jwtKeys.refreshSecretKey)
        val token = repository.findByTokenHash(hash)
            ?: throw RefreshTokenInvalidException()

        return token
            .ensureNotExpired(now = clockProvider.now())
            .ensureNotRevoked()
    }

    suspend fun findUserId(refreshToken: RawRefreshToken): String {
        val hash = hasher.hash(refreshToken.value, jwtKeys.refreshSecretKey)
        val token = repository.findByTokenHash(hash)
            ?: throw RefreshTokenInvalidException()

        return token.userId
    }

    suspend fun revokeAllTokensByUserId(userId: String) {
        repository.revokeAllActiveByUserId(userId)
    }
}