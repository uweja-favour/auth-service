package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.AccessTokenDocument
import com.xapps.time.types.KotlinInstant
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoAccessTokenRepository :
    CoroutineCrudRepository<AccessTokenDocument, String> {

    suspend fun findByJti(jti: String): AccessTokenDocument?

    suspend fun deleteAllByUserId(userId: String): Long

    suspend fun deleteByExpiryAtBeforeOrRevokedIsTrue(
        expiryAt: KotlinInstant,
        revoked: Boolean = true
    ): Long

    fun findAllByUserIdAndRevokedFalse(userId: String): Flow<AccessTokenDocument>
}