package com.xapps.auth.persistence.repository.user

import com.xapps.auth.persistence.entity.user.UserDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoUserRepository : CoroutineCrudRepository<UserDocument, String> {

    suspend fun findByUserId(userId: String): UserDocument?

    suspend fun findByEmail(email: String): UserDocument?

    suspend fun existsByEmail(email: String): Boolean
}