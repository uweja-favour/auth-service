//package com.xapps.auth.persistence.repository
//
//import com.xapps.auth.persistence.entity.UsedGooglePlaySubscriptionPurchaseTokenEntity
//import com.xapps.auth.domain.model.UsedGooglePlaySubscriptionPurchaseToken
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.toList
//import org.jetbrains.exposed.v1.core.ResultRow
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.core.and
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.jetbrains.exposed.v1.r2dbc.upsert
//import org.springframework.stereotype.Repository
//
//interface UsedGooglePlayPurchaseTokenRepository {
//    suspend fun save(token: UsedGooglePlaySubscriptionPurchaseToken): UsedGooglePlaySubscriptionPurchaseToken
//    suspend fun deactivateAllForUser(userId: String): Int
//    suspend fun findAllForUser(userId: String): List<UsedGooglePlaySubscriptionPurchaseToken>
//    suspend fun findAllByIsActive(userId: String, isActive: Boolean = true): List<UsedGooglePlaySubscriptionPurchaseToken>
//}
//
//@Repository
//class UsedGooglePlayPurchaseTokenRepositoryImpl(
//    private val db: R2dbcDatabase
//) : UsedGooglePlayPurchaseTokenRepository {
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    override suspend fun save(token: UsedGooglePlaySubscriptionPurchaseToken): UsedGooglePlaySubscriptionPurchaseToken =
//        suspendTransaction(
//            db = db
//        ) {
//            UsedGooglePlaySubscriptionPurchaseTokenEntity.upsert {
//                it[UsedGooglePlaySubscriptionPurchaseTokenEntity.purchaseToken] = token.purchaseToken
//                it[UsedGooglePlaySubscriptionPurchaseTokenEntity.subscriptionId] = token.subscriptionId
//                it[UsedGooglePlaySubscriptionPurchaseTokenEntity.packageName] = token.packageName
//                it[UsedGooglePlaySubscriptionPurchaseTokenEntity.userId] = token.userId
//                it[UsedGooglePlaySubscriptionPurchaseTokenEntity.lastExpiryMillis] = token.lastExpiryMillis
//                it[UsedGooglePlaySubscriptionPurchaseTokenEntity.isActive] = token.isActive
//            }
//            token
//        }
//
//    override suspend fun deactivateAllForUser(userId: String) =
//        suspendTransaction(
//            db = db
//        ) {
//            UsedGooglePlaySubscriptionPurchaseTokenEntity.update( { UsedGooglePlaySubscriptionPurchaseTokenEntity.userId eq userId}, limit = 1) {
//                it[isActive] = false
//            }
//        }
//
//    override suspend fun findAllForUser(userId: String) =
//        suspendTransaction(
//            db = db
//        ) {
//            UsedGooglePlaySubscriptionPurchaseTokenEntity.selectAll()
//                .where { UsedGooglePlaySubscriptionPurchaseTokenEntity.userId eq userId }
//                .map { toDomain(it) }
//                .toList()
//        }
//
//    override suspend fun findAllByIsActive(userId: String, isActive: Boolean) =
//        suspendTransaction(
//            db = db
//        ) {
//            UsedGooglePlaySubscriptionPurchaseTokenEntity.selectAll()
//                .where {
//                    (UsedGooglePlaySubscriptionPurchaseTokenEntity.userId eq userId) and
//                            (UsedGooglePlaySubscriptionPurchaseTokenEntity.isActive eq isActive)
//                }
//                .map { toDomain(it) }
//                .toList()
//        }
//
//    private fun toDomain(row: ResultRow) = UsedGooglePlaySubscriptionPurchaseToken(
//        purchaseToken = row[UsedGooglePlaySubscriptionPurchaseTokenEntity.purchaseToken],
//        subscriptionId = row[UsedGooglePlaySubscriptionPurchaseTokenEntity.subscriptionId],
//        packageName = row[UsedGooglePlaySubscriptionPurchaseTokenEntity.packageName],
//        userId = row[UsedGooglePlaySubscriptionPurchaseTokenEntity.userId],
//        lastExpiryMillis = row[UsedGooglePlaySubscriptionPurchaseTokenEntity.lastExpiryMillis],
//        isActive = row[UsedGooglePlaySubscriptionPurchaseTokenEntity.isActive]
//    )
//}
