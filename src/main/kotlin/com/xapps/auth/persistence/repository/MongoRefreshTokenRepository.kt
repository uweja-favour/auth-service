package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.RefreshTokenDocument
import com.xapps.time.types.KotlinInstant
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoRefreshTokenRepository :
    CoroutineCrudRepository<RefreshTokenDocument, String> {

    suspend fun findByTokenHash(tokenHash: String): RefreshTokenDocument?

    suspend fun deleteAllByUserId(userId: String): Long

    suspend fun findAllByUserIdAndRevoked(userId: String, revoked: Boolean): List<RefreshTokenDocument>

    suspend fun countByUserIdAndRevoked(userId: String, revoked: Boolean): Long

    suspend fun deleteByRevokedIsTrueAndExpiryAtBefore(
        expiryAt: KotlinInstant
    ): Long

//    suspend fun updateAllByUserIdAndRevokedIsFalse(
//        userId: String,
//        revoked: Boolean = true
//    ): Long
//
//    suspend fun updateByTokenHashAndRevokedIsFalse(
//        tokenHash: String,
//        revoked: Boolean = true
//    ): Long

    @Query("{ 'userId': ?0, 'revoked': false }")
    @Update("{ '\$set': { 'revoked': ?1 } }")
    suspend fun revokeAllByUserId(
        userId: String,
        revoked: Boolean = true
    ): Long

    @Query("{ 'tokenHash': ?0, 'revoked': false }")
    @Update("{ '\$set': { 'revoked': ?1 } }")
    suspend fun revokeByTokenHash(
        tokenHash: String,
        revoked: Boolean = true
    ): Long
}