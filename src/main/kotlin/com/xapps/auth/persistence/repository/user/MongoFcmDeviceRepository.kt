package com.xapps.auth.persistence.repository.user

import com.xapps.auth.persistence.entity.user.FcmDeviceDocument
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoFcmDeviceRepository : CoroutineCrudRepository<FcmDeviceDocument, String> {

    suspend fun findByToken(token: String): FcmDeviceDocument?

    fun findAllByUserId(userId: String): Flow<FcmDeviceDocument>

    suspend fun deleteByToken(token: String): Long
}