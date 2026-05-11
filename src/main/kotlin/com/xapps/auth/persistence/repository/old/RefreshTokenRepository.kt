//@file:OptIn(ExperimentalTime::class)
//
//package com.xapps.auth.persistence.repository
//
//import com.xapps.auth.persistence.entity.RefreshTokenEntity
//import com.xapps.auth.infrastructure.security.model.RefreshToken
//import com.xapps.time.types.KotlinInstant
//import com.xapps.time.types.KotlinLocalDateTime
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.singleOrNull
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.withContext
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toInstant
//import kotlinx.datetime.toLocalDateTime
//import org.jetbrains.exposed.v1.core.ResultRow
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.core.and
//import org.jetbrains.exposed.v1.r2dbc.*
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Repository
//import kotlin.time.ExperimentalTime
//
//interface RefreshTokenRepository {
//    suspend fun insert(token: RefreshToken): RefreshToken
//    suspend fun update(token: RefreshToken): RefreshToken
//    suspend fun saveAll(tokens: List<RefreshToken>): List<RefreshToken>
//    suspend fun findByTokenHash(tokenHash: String): RefreshToken?
//    suspend fun revokeAllActiveByUserId(userId: String): Int
//    suspend fun revokeByTokenHash(tokenHash: String): Int
//    suspend fun deleteAllByUserId(userId: String): Int
//    suspend fun findAllByUserIdAndRevoked(userId: String, revoked: Boolean): List<RefreshToken>
//    suspend fun countActiveByUserId(userId: String): Long
//    suspend fun deleteExpiredAndRevoked(cutoff: KotlinLocalDateTime): Int
//}
//
//@Repository
//class RefreshTokenRepositoryImpl(
//    private val db: R2dbcDatabase
//): RefreshTokenRepository {
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//
//    companion object {
//        private val ioDispatcher = Dispatchers.IO
//        private fun KotlinInstant.convertToLocalDateTime(): KotlinLocalDateTime = this.toLocalDateTime(TimeZone.UTC)
//        private fun KotlinLocalDateTime.convertToInstant() = this.toInstant(TimeZone.UTC)
//    }
//
//    override suspend fun update(token: RefreshToken): RefreshToken =
//        suspendTransaction(
//            db = db
//        ) {
//            RefreshTokenEntity.update( { RefreshTokenEntity.id eq token.id } ) {
//                it[RefreshTokenEntity.id] = token.id
//                it[RefreshTokenEntity.userId] = token.userId
//                it[RefreshTokenEntity.jti] = token.jti
//                it[RefreshTokenEntity.tokenHash] = token.tokenHash
//                it[RefreshTokenEntity.createdAt] = token.createdAt
//                it[RefreshTokenEntity.lastUsedAt] = token.lastUsedAt
//                it[RefreshTokenEntity.deviceIp] = token.deviceIp
//                it[RefreshTokenEntity.userAgent] = token.userAgent
//                it[RefreshTokenEntity.expiryAt] = token.expiryAt.convertToLocalDateTime()
//                it[RefreshTokenEntity.revoked] = token.revoked
//            }
//            return@suspendTransaction token
//        }
//
//    override suspend fun insert(token: RefreshToken): RefreshToken {
//        logger.info("About to insert refresh token.")
//        suspendTransaction(
//            db = db
//        ) {
//            RefreshTokenEntity.insert {
//                it[id] = token.id
//                it[userId] = token.userId
//                it[jti] = token.jti
//                it[tokenHash] = token.tokenHash
//                it[createdAt] = token.createdAt
//                it[lastUsedAt] = token.lastUsedAt
//                it[deviceIp] = token.deviceIp
//                it[userAgent] = token.userAgent
//                it[expiryAt] = token.expiryAt.convertToLocalDateTime()
//                it[revoked] = token.revoked
//            }
//        }
//        logger.info("Finished insert refresh token OP")
//        return token
//    }
//
//    override suspend fun saveAll(tokens: List<RefreshToken>): List<RefreshToken> =
//        suspendTransaction(
//            db = db
//        ) {
//            val results = RefreshTokenEntity.batchReplace(tokens) { token ->
//                this[RefreshTokenEntity.id] = token.id
//                this[RefreshTokenEntity.userId] = token.userId
//                this[RefreshTokenEntity.jti] = token.jti
//                this[RefreshTokenEntity.tokenHash] = token.tokenHash
//                this[RefreshTokenEntity.createdAt] = token.createdAt
//                this[RefreshTokenEntity.lastUsedAt] = token.lastUsedAt
//                this[RefreshTokenEntity.deviceIp] = token.deviceIp
//                this[RefreshTokenEntity.userAgent] = token.userAgent
//                this[RefreshTokenEntity.expiryAt] = token.expiryAt.convertToLocalDateTime()
//                this[RefreshTokenEntity.revoked] = token.revoked
//            }
//            return@suspendTransaction results.map { toDomain(it) }
//        }
//
//    override suspend fun findByTokenHash(tokenHash: String): RefreshToken? {
//        return withContext(Dispatchers.IO) {
//            suspendTransaction(db = db) {
//
//                logger.info("Coroutine context: $coroutineContext")
//
//                val row = RefreshTokenEntity
//                    .selectAll()
//                    .where { RefreshTokenEntity.tokenHash eq tokenHash }
//                    .singleOrNull()
//
//                row?.let {
//                    logger.info("Found token row: $it")
//                    toDomain(it)
//                }
//            }
//        }
//    }
//
//    override suspend fun revokeAllActiveByUserId(userId: String) =
//        suspendTransaction(db = db) {
//            return@suspendTransaction RefreshTokenEntity.update({
//                (RefreshTokenEntity.userId eq userId) and (RefreshTokenEntity.revoked eq false)
//            }) { it[revoked] = true }
//        }
//
//    override suspend fun revokeByTokenHash(tokenHash: String) =
//        suspendTransaction(db = db) {
//            return@suspendTransaction RefreshTokenEntity.update({
//                (RefreshTokenEntity.tokenHash eq tokenHash) and (RefreshTokenEntity.revoked eq false)
//            }) { it[revoked] = true }
//        }
//
//    override suspend fun deleteAllByUserId(userId: String) =
//        suspendTransaction(db = db) {
//            return@suspendTransaction RefreshTokenEntity.deleteWhere { RefreshTokenEntity.userId eq userId }
//        }
//
//    override suspend fun findAllByUserIdAndRevoked(userId: String, revoked: Boolean) =
//        suspendTransaction(db = db) {
//            return@suspendTransaction RefreshTokenEntity.selectAll()
//                .where { (RefreshTokenEntity.userId eq userId) and (RefreshTokenEntity.revoked eq revoked) }
//                .map { toDomain(it) }.toList()
//        }
//
//    override suspend fun countActiveByUserId(userId: String) =
//        suspendTransaction(db = db) {
//            return@suspendTransaction RefreshTokenEntity.select (
//                (RefreshTokenEntity.userId eq userId) and (RefreshTokenEntity.revoked eq false)
//            ).count()
//        }
//
//    override suspend fun deleteExpiredAndRevoked(cutoff: KotlinLocalDateTime) =
//        suspendTransaction(db = db) {
//            return@suspendTransaction RefreshTokenEntity.deleteWhere {
//                (revoked eq true) and (expiryAt less cutoff)
//            }
//        }
//
//    private fun toDomain(row: ResultRow) = RefreshToken(
//        id = row[RefreshTokenEntity.id],
//        userId = row[RefreshTokenEntity.userId],
//        jti = row[RefreshTokenEntity.jti],
//        tokenHash = row[RefreshTokenEntity.tokenHash],
//        createdAt = row[RefreshTokenEntity.createdAt],
//        lastUsedAt = row[RefreshTokenEntity.lastUsedAt],
//        deviceIp = row[RefreshTokenEntity.deviceIp],
//        userAgent = row[RefreshTokenEntity.userAgent],
//        expiryAt = row[RefreshTokenEntity.expiryAt].convertToInstant(),
//        revoked = row[RefreshTokenEntity.revoked]
//    )
//}