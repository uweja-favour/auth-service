package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.infrastructure.security.model.RefreshToken
import com.xapps.auth.persistence.entity.RefreshTokenDocument
import com.xapps.auth.persistence.repository.MongoRefreshTokenRepository
import com.xapps.auth.persistence.saveAllUpserting
import com.xapps.auth.persistence.saveUpserting
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Repository

interface RefreshTokenRepository {

    suspend fun insert(token: RefreshToken): RefreshToken

    suspend fun update(token: RefreshToken): RefreshToken

    suspend fun saveAll(tokens: List<RefreshToken>): List<RefreshToken>

    suspend fun findByTokenHash(tokenHash: String): RefreshToken?

    suspend fun revokeAllActiveByUserId(userId: String): Long

    suspend fun revokeByTokenHash(tokenHash: String): Long

    suspend fun deleteAllByUserId(userId: String): Long

    suspend fun findAllByUserIdAndRevoked(
        userId: String,
        revoked: Boolean
    ): List<RefreshToken>

    suspend fun countActiveByUserId(userId: String): Long

    suspend fun deleteExpiredAndRevoked(cutoff: KotlinInstant): Long
}

@Repository
class RefreshTokenRepositoryImpl(
    private val mongoRepository: MongoRefreshTokenRepository
) : RefreshTokenRepository {

    override suspend fun insert(token: RefreshToken): RefreshToken {
        mongoRepository.saveUpserting(token.toDocument())
        return token
    }

    override suspend fun update(token: RefreshToken): RefreshToken {
        mongoRepository.saveUpserting(token.toDocument())
        return token
    }

    override suspend fun saveAll(tokens: List<RefreshToken>): List<RefreshToken> {
        mongoRepository.saveAllUpserting(tokens.map { it.toDocument() })
        return tokens
    }

    override suspend fun findByTokenHash(tokenHash: String): RefreshToken? {
        return mongoRepository.findByTokenHash(tokenHash)?.toDomain()
    }

    override suspend fun revokeAllActiveByUserId(userId: String): Long {
        return mongoRepository.revokeAllByUserId(
            userId = userId,
            revoked = true
        )
    }

    override suspend fun revokeByTokenHash(tokenHash: String): Long {
        return mongoRepository.revokeByTokenHash(
            tokenHash = tokenHash,
            revoked = true
        )
    }

    override suspend fun deleteAllByUserId(userId: String): Long {
        return mongoRepository.deleteAllByUserId(userId)
    }

    override suspend fun findAllByUserIdAndRevoked(
        userId: String,
        revoked: Boolean
    ): List<RefreshToken> {
        return mongoRepository
            .findAllByUserIdAndRevoked(userId, revoked)
            .map { it.toDomain() }
    }

    override suspend fun countActiveByUserId(userId: String): Long {
        return mongoRepository.countByUserIdAndRevoked(userId, false)
    }

    override suspend fun deleteExpiredAndRevoked(cutoff: KotlinInstant): Long {
        return mongoRepository.deleteByRevokedIsTrueAndExpiryAtBefore(cutoff)
    }

    private fun RefreshToken.toDocument(): RefreshTokenDocument {
        return RefreshTokenDocument(
            id1 = id,
            userId = userId,
            jti = jti,
            tokenHash = tokenHash,
            createdAt = createdAt,
            lastUsedAt = lastUsedAt,
            deviceIp = deviceIp,
            userAgent = userAgent,
            expiryAt = expiryAt,
            revoked = revoked
        )
    }

    private fun RefreshTokenDocument.toDomain(): RefreshToken {
        return RefreshToken(
            id = id1,
            userId = userId,
            jti = jti,
            tokenHash = tokenHash,
            createdAt = createdAt,
            lastUsedAt = lastUsedAt,
            deviceIp = deviceIp,
            userAgent = userAgent,
            expiryAt = expiryAt,
            revoked = revoked
        )
    }
}