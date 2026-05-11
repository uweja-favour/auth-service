package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.NotificationDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoNotificationRepository :
    CoroutineCrudRepository<NotificationDocument, String> {

    suspend fun findAllByUserIdAndReadIsFalseOrderByInstantDesc(
        userId: String
    ): List<NotificationDocument>

    suspend fun deleteAllByUserId(userId: String): Long
}