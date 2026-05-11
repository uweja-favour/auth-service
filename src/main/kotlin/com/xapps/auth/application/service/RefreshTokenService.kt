//@file:OptIn(ExperimentalTime::class)
//
//package com.xapps.auth.application.service
//
//import com.xapps.auth.infrastructure.security.model.RefreshToken
//import com.xapps.auth.dto.RawRefreshToken
//import com.xapps.auth.persistence.repository.RefreshTokenRepository
//import io.jsonwebtoken.*
//import io.jsonwebtoken.security.Keys
//import jakarta.annotation.PostConstruct
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.stereotype.Service
//import java.nio.charset.StandardCharsets
//import java.security.SecureRandom
//import java.time.Instant
//import java.util.*
//import javax.crypto.Mac
//import javax.crypto.SecretKey
//import javax.crypto.spec.SecretKeySpec
//import kotlin.time.Duration.Companion.days
//import kotlin.time.ExperimentalTime
//import kotlin.time.toKotlinInstant
//
//@Service
//class RefreshTokenService(
//    private val refreshTokenRepository: RefreshTokenRepository
//) {
//    @Value("\${jwt.refreshSecret}")
//    private lateinit var refreshSecret: String
//
//    @Value("\${jwt.refreshToken.expiration-ms:2592000000}") // 30 days default
//    private var refreshTokenDuration: Long = 30.days.inWholeMilliseconds
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//    private val secureRandom = SecureRandom()
//    private lateinit var signingKey: SecretKey
//
//    @PostConstruct
//    fun init() {
//        require(refreshSecret.length >= 32) { "JWT refresh secret must be at least 32 characters long." }
//        signingKey = Keys.hmacShaKeyFor(refreshSecret.toByteArray(StandardCharsets.UTF_8))
//        logger.info("Refresh token signing key initialized successfully.")
//    }
//
//    private fun hmacSha256Hex(secret: String, data: String): String =
//        Mac.getInstance(HMAC_SHA_256).run {
//            init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), HMAC_SHA_256))
//            doFinal(data.toByteArray(Charsets.UTF_8))
//                .joinToString("") { "%02x".format(it) }
//        }
//
//    private fun generateJti(): String =
//        ByteArray(16).apply(secureRandom::nextBytes)
//            .let {
//                Base64.getUrlEncoder()
//                    .withoutPadding()
//                    .encodeToString(it)
//            }
//
//    /**
//     * Create a new refresh token for user and record device info.
//     * Returns the raw JWT string to send to the client.
//     */
//    suspend fun createNewRefreshToken(
//        userId: String,
//        deviceIp: String? = null,
//        userAgent: String? = null
//    ): RawRefreshToken {
//        // Revoke existing tokens atomically
//        logger.info("Refresh Service: About to revokeAllActiveByUserId")
//        refreshTokenRepository.revokeAllActiveByUserId(userId)
//
//        val now = Instant.now()
//        val expiry = now.plusMillis(refreshTokenDuration)
//        val jti = generateJti()
//
//        // Create JWT with exp claim
//        val token = Jwts.builder()
//            .setSubject(userId)
//            .setId(jti) // jti claim to make token unique
//            .setIssuedAt(Date.from(now))
//            .setExpiration(Date.from(expiry))
//            .claim(TOKEN_TYPE, REFRESH)
//            .setIssuer(ISSUER)
//            .signWith(signingKey, SignatureAlgorithm.HS512)
//            .compact()
//
//        val tokenHash = hmacSha256Hex(refreshSecret, token)
//
//        val refreshToken = RefreshToken(
//            userId = userId,
//            jti = jti,
//            tokenHash = tokenHash,
//            createdAt = now.toKotlinLocalDateTime(),
//            lastUsedAt = null,
//            deviceIp = deviceIp,
//            userAgent = userAgent,
//            expiryAt = expiry.toKotlinInstant(),
//            revoked = false
//        )
//
//        logger.info("Created new refresh token for user=$userId jti=$jti expiry=$expiry deviceIp=${deviceIp ?: "unknown"}")
//        refreshTokenRepository.insert(refreshToken)
//
//        logger.info("Returning from inserting the new refresh token")
//        return RawRefreshToken.fromString(token)
//    }
//
//    /**
//     * Verifies an incoming refresh token.
//     *
//     * Steps:
//     *  1. Parse and verify the JWT.
//     *  2. Look up its hash in the DB (fast lookup, avoids scanning).
//     *  3. Handle duplicates defensively.
//     *  4. Check revocation, expiry, and subject integrity.
//     *  5. Update metadata and return a verified token.
//     *
//     * Throws [IllegalArgumentException] for invalid tokens,
//     * and [IllegalStateException] for expired or revoked ones.
//     */
//    suspend fun verifyRefreshToken(
//        raw: RawRefreshToken,
//        ip: String? = null,
//        userAgent: String? = null
//    ): RefreshToken {
//        val hash = hmacSha256Hex(refreshSecret, raw.value)
//        logger.info("🔍 Verifying refresh token (hash prefix=${hash.take(10)})")
//
//        val claims = try {
//            parseClaims(raw)
//        } catch (ex: ExpiredJwtException) {
//            logger.info("Token expired during parse: ${ex.message}")
//            null
//        }
//
//        logger.info("About to execute repository call")
//        val tokens = refreshTokenRepository.findAllByTokenHash(hash)
//        if (tokens.isEmpty()) {
//            logger.warn("❌ Token not found (hash prefix=${hash.take(12)})")
//            throw IllegalArgumentException("Invalid refresh token")
//        }
//
//        // Deduplicate tokens (defensive cleanup)
//        if (tokens.size > 1) {
//            logger.error("🚨 Duplicate refresh tokens for hash=$hash; revoking extras")
//            refreshTokenRepository.saveAll(
//                tokens.drop(1).filterNot { it.revoked }.map { it.copy(revoked = true) }
//            )
//        }
//
//        val token = tokens.first()
//
//        // Replay / reuse detection
//        if (token.revoked) {
//            logger.warn("🚨 Reuse detected for user=${token.userId} jti=${token.jti} ip=${ip ?: "unknown"}")
//            handleReplayDetection(token)
//            throw IllegalStateException("Refresh token has been revoked")
//        }
//
//        // Token failed to parse or was expired during parse
//        if (claims == null) {
//            refreshTokenRepository.update(token.copy(revoked = true))
//            logger.info("🔒 Marked expired token revoked for user=${token.userId} jti=${token.jti}")
//            throw IllegalStateException("Refresh token has expired or is invalid")
//        }
//
//        // Expiration check (trust JWT's canonical expiry)
//        val now = Instant.now()
//        if (claims.expiration.toInstant().isBefore(now)) {
//            refreshTokenRepository.update(token.copy(revoked = true))
//            logger.info("🔒 Token expired – revoked for user=${token.userId} jti=${token.jti}")
//            throw IllegalStateException("Refresh token has expired")
//        }
//
//        // Subject integrity check
//        if (claims.subject != token.userId) {
//            logger.error("⚠️ Token subject mismatch: JWT=${claims.subject}, DB=${token.userId}")
//            handleReplayDetection(token)
//            throw IllegalArgumentException("Invalid token subject")
//        }
//
//        // Update metadata immutably
//        val updated = token.copy(
//            lastUsedAt = now.toKotlinLocalDateTime(),
//            deviceIp = ip?.takeIf { it.isNotBlank() } ?: token.deviceIp,
//            userAgent = userAgent?.takeIf { it.isNotBlank() } ?: token.userAgent
//        )
//
//        refreshTokenRepository.update(updated)
//        logger.info("✅ Verified refresh token for user=${token.userId} jti=${token.jti} ip=${ip ?: "unknown"}")
//
//        return updated
//    }
//
//    private fun parseClaims(rawRefreshToken: RawRefreshToken): Claims {
//        val claims = try {
//            Jwts.parserBuilder()
//                .setSigningKey(signingKey)
//                .build()
//                .parseClaimsJws(rawRefreshToken.value)
//                .body
//
//        } catch (e: ExpiredJwtException) {
//            logger.debug("JWT expired while parsing: ${e.message}")
//            throw e // allow caller to handle and map to IllegalStateException
//        } catch (e: UnsupportedJwtException) {
//            logger.debug("Unsupported JWT token: ${e.message}")
//            throw IllegalArgumentException("Unsupported token", e)
//        } catch (e: MalformedJwtException) {
//            logger.debug("Malformed JWT token: ${e.message}")
//            throw IllegalArgumentException("Malformed token", e)
//        } catch (e: SignatureException) {
//            logger.debug("Invalid JWT signature: ${e.message}")
//            throw IllegalArgumentException("Invalid signature", e)
//        } catch (e: IllegalArgumentException) {
//            logger.debug("Invalid JWT token: ${e.message}")
//            throw IllegalArgumentException("Invalid token", e)
//        }
//
//        if (claims[TOKEN_TYPE] != REFRESH) {
//            throw IllegalArgumentException("Token is not a refresh token")
//        }
//
//        return claims
//    }
//
//    suspend fun revokeAndRotateRefreshToken(
//        oldToken: RefreshToken,
//        deviceIp: String? = null,
//        userAgent: String? = null
//    ): RawRefreshToken = createNewRefreshToken(oldToken.userId, deviceIp, userAgent)
//
//    suspend fun revokeAllRefreshTokensForUser(userId: String) {
//        val count = refreshTokenRepository.revokeAllActiveByUserId(userId)
//        logger.info("✅ Revoked $count refresh tokens for user=$userId")
//    }
//
//    // MARK: Helpers & incident handling
//    private suspend fun handleReplayDetection(replayed: RefreshToken) {
//        // 1) revoke all tokens for user
//        refreshTokenRepository.revokeAllActiveByUserId(replayed.userId)
//
//        // 2) emit alert/incident to monitoring/ops - implement your own integration
//        logger.warn("🔔 ALERT: token replay detected for user=${replayed.userId} jti=${replayed.jti}; revoking all tokens and escalating")
//
//        // TODO: integrate with alerting/incident system (PagerDuty, OpsGenie) + send user notification (email/push)
//        // Example: alertService.createIncident("token_replay", details)
//    }
//
//    @Scheduled(cron = "0 0 3 * * ?") // Every day at 3AM
//    private suspend fun cleanUpExpiredTokens() {
//        val now = Instant.now()
//        val gracePeriod = 30.days
//
//        // Tokens will only be deleted if expiryDate < (now - 30 days)
//        val cutoff = now.minusMillis(gracePeriod.inWholeMilliseconds)
//
//        val deletedCount = refreshTokenRepository.deleteExpiredAndRevoked(cutoff.toKotlinLocalDateTime())
//
//        if (deletedCount > 0) {
//            logger.info("🧹 Cleaned up $deletedCount expired/revoked refresh tokens (grace period = 30 days)")
//        }
//    }
//
//    suspend fun getTokenCountForUser(userId: String): Long =
//        refreshTokenRepository.countActiveByUserId(userId)
//
//    private companion object {
//        private const val HMAC_SHA_256 = "HmacSHA256"
//        private const val TOKEN_TYPE = "token_type"
//        private const val REFRESH = "refresh"
//        private const val ISSUER = "xapps.studentapplication"
//    }
//}