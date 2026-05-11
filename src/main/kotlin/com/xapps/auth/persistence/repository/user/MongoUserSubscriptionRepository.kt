package com.xapps.auth.persistence.repository.user

import com.xapps.auth.persistence.entity.user.UserSubscriptionDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoUserSubscriptionRepository :
    CoroutineCrudRepository<UserSubscriptionDocument, String> {

    suspend fun findByUserId(userId: String): UserSubscriptionDocument?

    suspend fun deleteByUserId(userId: String): Long
}