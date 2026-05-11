//package com.xapps.auth.persistence.repository
//
//import com.xapps.auth.persistence.entity.NotificationEntity
//import com.xapps.auth.domain.model.Notification
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.toList
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.deleteWhere
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.jetbrains.exposed.v1.r2dbc.upsert
//import org.springframework.stereotype.Repository
//
//interface NotificationRepository {
//    suspend fun save(notification: Notification): Notification
//    suspend fun findUnreadByUserIdOrderByTimestampDesc(userId: String): List<Notification>
//    suspend fun deleteAllUserNotifications(userId: String): Int
//    suspend fun markAllAsRead(userId: String, ids: List<Long>): Int
//    suspend fun markAsRead(userId: String, notificationId: Long): Int
//}
//
//@Repository
//class NotificationRepositoryImpl(
//    private val db: R2dbcDatabase
//) : NotificationRepository {
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    override suspend fun save(notification: Notification): Notification =
//        suspendTransaction(
//            db = db
//        ) {
//            NotificationEntity.upsert {
//                it[NotificationEntity.userId] = notification.userId
//                it[NotificationEntity.title] = notification.title
//                it[NotificationEntity.message] = notification.message
//                it[NotificationEntity.type] = notification.type
//                it[NotificationEntity.datetime] = notification.datetime
//                it[read] = notification.read
//            }
//
//            notification
//        }
//
//    override suspend fun findUnreadByUserIdOrderByTimestampDesc(userId: String): List<Notification> =
//        suspendTransaction(
//            db = db
//        ) {
//            NotificationEntity
//                .selectAll()
//                .where { NotificationEntity.userId eq userId and (NotificationEntity.read eq false) }
//                .orderBy(NotificationEntity.datetime, SortOrder.DESC)
//                .map { toDomain(it) }
//                .toList()
//        }
//
//    override suspend fun deleteAllUserNotifications(userId: String): Int =
//        suspendTransaction(
//            db = db
//        ) {
//            NotificationEntity.deleteWhere { NotificationEntity.userId eq userId }
//        }
//
//    override suspend fun markAllAsRead(userId: String, ids: List<Long>): Int =
//        suspendTransaction(
//            db = db
//        ) {
//            NotificationEntity.update({ (NotificationEntity.userId eq userId) and (NotificationEntity.id inList ids) }) {
//                it[read] = true
//            }
//        }
//
//    override suspend fun markAsRead(userId: String, notificationId: Long): Int =
//        suspendTransaction(
//            db = db
//        ) {
//            NotificationEntity.update({ (NotificationEntity.userId eq userId) and (NotificationEntity.id eq notificationId) }) {
//                it[read] = true
//            }
//        }
//
//    private fun toDomain(row: ResultRow) = Notification(
//        id = row[NotificationEntity.id],
//        userId = row[NotificationEntity.userId],
//        title = row[NotificationEntity.title],
//        message = row[NotificationEntity.message],
//        type = row[NotificationEntity.type],
//        datetime = row[NotificationEntity.datetime],
//        read = row[NotificationEntity.read]
//    )
//}
