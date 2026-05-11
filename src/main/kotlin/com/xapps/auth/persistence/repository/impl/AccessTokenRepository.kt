package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.infrastructure.security.model.AccessToken
import com.xapps.auth.persistence.entity.AccessTokenDocument
import com.xapps.auth.persistence.repository.MongoAccessTokenRepository
import com.xapps.auth.persistence.saveUpserting
import com.xapps.time.types.KotlinInstant
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.springframework.stereotype.Repository

interface AccessTokenRepository {

    suspend fun insert(token: AccessToken): AccessToken

    suspend fun update(token: AccessToken): AccessToken

    suspend fun findByJti(jti: String): AccessToken?

    suspend fun deleteAllByUserId(userId: String): Long

    suspend fun deleteExpiredOrRevokedBefore(cutoff: KotlinInstant): Long

    suspend fun revokeAllActiveByUserId(userId: String): Long
}

@Repository
class AccessTokenRepositoryImpl(
    private val repository: MongoAccessTokenRepository
) : AccessTokenRepository {

    override suspend fun insert(token: AccessToken): AccessToken {
        repository.saveUpserting(token.toDocument())
        return token
    }

    override suspend fun update(token: AccessToken): AccessToken {
        repository.saveUpserting(token.toDocument())
        return token
    }

    override suspend fun findByJti(jti: String): AccessToken? {
        return repository.findByJti(jti)?.toDomain()
    }

    override suspend fun deleteAllByUserId(userId: String): Long {
        return repository.deleteAllByUserId(userId)
    }

    override suspend fun deleteExpiredOrRevokedBefore(cutoff: KotlinInstant): Long {
        return repository.deleteByExpiryAtBeforeOrRevokedIsTrue(
            expiryAt = cutoff,
            revoked = true
        )
    }

    override suspend fun revokeAllActiveByUserId(userId: String): Long {
        return repository.findAllByUserIdAndRevokedFalse(userId)
            .map { it.copy(revoked = true) }
            .onEach { repository.saveUpserting(it) }
            .count()
            .toLong()
    }

    private fun AccessToken.toDocument(): AccessTokenDocument {
        return AccessTokenDocument(
            jti = jti,
            userId = userId,
            createdAt = createdAt,
            expiryAt = expiryAt,
            revoked = revoked
        )
    }

    private fun AccessTokenDocument.toDomain(): AccessToken {
        return AccessToken(
            jti = jti,
            userId = userId,
            createdAt = createdAt,
            expiryAt = expiryAt,
            revoked = revoked
        )
    }
}