//package com.xapps.auth.persistence.repository.user.old
//
//import com.xapps.auth.persistence.entity.user.UserSubscriptionEntity
//import com.xapps.auth.domain.model.user.UserSubscription
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.singleOrNull
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.*
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.springframework.stereotype.Repository
//
//interface UserSubscriptionRepository {
//    suspend fun insert(subscription: UserSubscription): UserSubscription
//    suspend fun update(subscription: UserSubscription): UserSubscription
//
//    suspend fun R2dbcTransaction.findByUserId(userId: String): UserSubscription?
//    suspend fun deleteByUserId(userId: String): Int
//}
//
//@Repository
//class UserSubscriptionRepositoryImpl(
//    private val db: R2dbcDatabase
//) : UserSubscriptionRepository {
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    /*** Used to create a new subscription for a user who never had a subscription before.
//     *   To renew a user subscription, see renewSubscription method
//     */
//    override suspend fun insert(subscription: UserSubscription): UserSubscription =
//        suspendTransaction(
//            db = db
//        ) {
//            val existing = UserSubscriptionEntity.selectAll()
//                .where { UserSubscriptionEntity.userId eq subscription.userId }
//                .singleOrNull()
//
//            if (existing != null) {
//                throw RuntimeException("User with id: ${subscription.userId} already has a subscription")
//            }
//
//            UserSubscriptionEntity.insert {
//                it[UserSubscriptionEntity.id] = subscription.id
//                it[UserSubscriptionEntity.userId] = subscription.userId
//                it[UserSubscriptionEntity.platform] = subscription.platform
//                it[UserSubscriptionEntity.plan] = subscription.plan
//                it[UserSubscriptionEntity.startAt] = subscription.startAt
//                it[UserSubscriptionEntity.endAt] = subscription.endAt
//            }
//            subscription
//    }
//
//    override suspend fun update(subscription: UserSubscription): UserSubscription =
//        suspendTransaction(
//            db = db
//        ) {
//            val existing = UserSubscriptionEntity.selectAll()
//                .where { UserSubscriptionEntity.userId eq subscription.userId }
//                .singleOrNull()
//
//            if (existing != null) {
//                throw RuntimeException("User with id: ${subscription.userId} already has a subscription")
//            }
//
//            UserSubscriptionEntity.update( { UserSubscriptionEntity.id eq subscription.id } ) {
//                it[UserSubscriptionEntity.id] = subscription.id
//                it[UserSubscriptionEntity.userId] = subscription.userId
//                it[UserSubscriptionEntity.platform] = subscription.platform
//                it[UserSubscriptionEntity.plan] = subscription.plan
//                it[UserSubscriptionEntity.startAt] = subscription.startAt
//                it[UserSubscriptionEntity.endAt] = subscription.endAt
//            }
//            subscription
//        }
//
//    override suspend fun R2dbcTransaction.findByUserId(userId: String) =
//        run {
//            UserSubscriptionEntity.select ( UserSubscriptionEntity.userId eq userId )
//                .map { toDomain(it) }
//                .singleOrNull()
//        }
//
//    override suspend fun deleteByUserId(userId: String) =
//        suspendTransaction(
//            db = db
//        ) {
//            UserSubscriptionEntity.deleteWhere { UserSubscriptionEntity.userId eq userId }
//        }
//
//    private fun toDomain(row: ResultRow) = UserSubscription(
//        id = row[UserSubscriptionEntity.id],
//        userId = row[UserSubscriptionEntity.userId],
//        platform = row[UserSubscriptionEntity.platform],
//        plan = row[UserSubscriptionEntity.plan],
//        startAt = row[UserSubscriptionEntity.startAt],
//        endAt = row[UserSubscriptionEntity.endAt],
//        lastRenewal = row[UserSubscriptionEntity.lastRenewal],
//    )
//}