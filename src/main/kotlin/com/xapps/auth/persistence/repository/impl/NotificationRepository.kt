package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.domain.model.Notification
import com.xapps.auth.domain.model.NotificationType
import com.xapps.auth.persistence.entity.NotificationDocument
import com.xapps.auth.persistence.repository.MongoNotificationRepository
import com.xapps.auth.persistence.repository.NotificationRepositoryCustom
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface NotificationRepository {
    suspend fun save(notification: Notification): Notification
    suspend fun findUnreadByUserIdOrderByTimestampDesc( userId: String ): List<Notification>
    suspend fun deleteAllUserNotifications(userId: String): Long
    suspend fun markAllAsRead(userId: String, ids: List<String>): Long
    suspend fun markAsRead(userId: String, notificationId: String): Long
}

@Repository
class NotificationRepositoryImpl(
    private val mongoRepository: MongoNotificationRepository,
    private val customRepository: NotificationRepositoryCustom
) : NotificationRepository {

    override suspend fun save(notification: Notification): Notification {
        mongoRepository.saveUpserting(notification.toDocument())
        return notification
    }

    override suspend fun findUnreadByUserIdOrderByTimestampDesc(
        userId: String
    ): List<Notification> {

        return mongoRepository
            .findAllByUserIdAndReadIsFalseOrderByInstantDesc(userId)
            .map { it.toDomain() }
    }

    override suspend fun deleteAllUserNotifications(userId: String): Long {
        return mongoRepository.deleteAllByUserId(userId)
    }

    override suspend fun markAllAsRead(
        userId: String,
        ids: List<String>
    ): Long {

        return customRepository.markAllAsRead(
            userId = userId,
            ids = ids
        )
    }

    override suspend fun markAsRead(
        userId: String,
        notificationId: String
    ): Long {

        return customRepository.markAsRead(
            userId = userId,
            id = notificationId
        )
    }

    private fun Notification.toDocument(): NotificationDocument {
        return NotificationDocument(
            id1 = id,
            userId = userId,
            title = title,
            message = message,
            notificationTypeCode = type.code,
            instant = instant,
            read = read
        )
    }

    private fun NotificationDocument.toDomain(): Notification {
        return Notification(
            id = id1,
            userId = userId,
            title = title,
            message = message,
            type = NotificationType.fromCode(notificationTypeCode),
            instant = instant,
            read = read
        )
    }
}