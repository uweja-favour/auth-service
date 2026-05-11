//@file:OptIn(ExperimentalTime::class)
//
//package com.xapps.auth.persistence.repository
//
//import com.xapps.auth.persistence.entity.AccessTokenEntity
//import com.xapps.auth.infrastructure.security.model.AccessToken
//import com.xapps.time.types.KotlinInstant
//import com.xapps.time.types.KotlinLocalDateTime
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.singleOrNull
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toInstant
//import kotlinx.datetime.toLocalDateTime
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.deleteWhere
//import org.jetbrains.exposed.v1.r2dbc.insert
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.springframework.stereotype.Repository
//import kotlin.time.ExperimentalTime
//
//interface AccessTokenRepository {
//    suspend fun insert(token: AccessToken): AccessToken
//    suspend fun update(token: AccessToken): AccessToken
//    suspend fun findByJti(jti: String): AccessToken?
//    suspend fun deleteAllByUserId(userId: String): Int
//    suspend fun deleteExpiredOrRevokedBefore(cutoff: KotlinLocalDateTime): Int
//    suspend fun revokeAllActiveByUserId(userId: String): Int
//}
//
//@Repository
//class AccessTokenRepositoryImpl(
//    private val db: R2dbcDatabase
//) : AccessTokenRepository {
//
//    companion object {
//        private val ioDispatcher = Dispatchers.IO
//        private fun KotlinInstant.convertToLocalDateTime() = this.toLocalDateTime(TimeZone.UTC)
//        private fun KotlinLocalDateTime.convertToInstant() = this.toInstant(TimeZone.UTC)
//    }
//
//    override suspend fun insert(token: AccessToken) =
//        suspendTransaction(
//            db = db
//        ) {
//             AccessTokenEntity.insert {
//                it[jti] = token.jti
//                it[userId] = token.userId
//                it[createdAt] = token.createdAt.convertToLocalDateTime()
//                it[expiryAt] = token.expiryAt.convertToLocalDateTime()
//                it[revoked] = token.revoked
//            }
//            return@suspendTransaction token // always return domain model
//        }
//
//    override suspend fun update(token: AccessToken) =
//        suspendTransaction(db) {
//            AccessTokenEntity.update({ AccessTokenEntity.jti eq token.jti }) {
//                it[userId] = token.userId
//                it[createdAt] = token.createdAt.convertToLocalDateTime()
//                it[expiryAt] = token.expiryAt.convertToLocalDateTime()
//                it[revoked] = token.revoked
//            }
//            return@suspendTransaction token // return domain model
//        }
//
//    override suspend fun findByJti(jti: String): AccessToken? =
//        suspendTransaction(
//            db = db
//        ) {
//            return@suspendTransaction AccessTokenEntity
//                .selectAll()
//                .where { AccessTokenEntity.jti eq id }
//                .singleOrNull()
//                ?.toAccessToken()
//        }
//
//    override suspend fun deleteAllByUserId(userId: String) =
//        suspendTransaction(
//            db = db
//        ) {
//            return@suspendTransaction AccessTokenEntity.deleteWhere { AccessTokenEntity.userId eq userId }
//        }
//
//    override suspend fun deleteExpiredOrRevokedBefore(cutoff: KotlinLocalDateTime) =
//        suspendTransaction(
//            db = db
//        ) {
//            return@suspendTransaction AccessTokenEntity.deleteWhere {
//                (expiryAt less cutoff) or (revoked eq true)
//            }
//        }
//
//    override suspend fun revokeAllActiveByUserId(userId: String) =
//        suspendTransaction(db = db) {
//
//            return@suspendTransaction AccessTokenEntity.update(
//                where = { (AccessTokenEntity.userId eq userId) and (AccessTokenEntity.revoked eq false) }
//            ) { it[revoked] = true }
//        }
//
//    private fun ResultRow.toAccessToken(): AccessToken {
//        val jti = this[AccessTokenEntity.jti]
//        val userId = this[AccessTokenEntity.userId]
//        val createdAt = this[AccessTokenEntity.createdAt]
//        val expiryAt = this[AccessTokenEntity.expiryAt]
//        val revoked = this[AccessTokenEntity.revoked]
//
//        return AccessToken(
//            jti = jti,
//            userId = userId,
//            createdAt = createdAt.convertToInstant(),
//            expiryAt = expiryAt.convertToInstant(),
//            revoked = revoked
//        )
//    }
//}