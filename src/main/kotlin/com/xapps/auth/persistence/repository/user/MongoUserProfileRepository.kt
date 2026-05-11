package com.xapps.auth.persistence.repository.user

import com.xapps.auth.persistence.entity.user.UserProfileDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoUserProfileRepository : CoroutineCrudRepository<UserProfileDocument, String> {

    suspend fun findByUserId(userId: String): UserProfileDocument?

    suspend fun deleteByUserId(userId: String): Long
}