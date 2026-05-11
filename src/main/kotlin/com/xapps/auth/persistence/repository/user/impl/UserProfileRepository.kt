package com.xapps.auth.persistence.repository.user.impl

import com.xapps.auth.domain.model.user.FcmDevice
import com.xapps.auth.domain.model.user.UserProfile
import com.xapps.auth.persistence.repository.user.MongoUserProfileRepository
import com.xapps.auth.persistence.entity.user.UserProfileDocument
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface UserProfileRepository {

    suspend fun create(userProfile: UserProfile): UserProfile

    suspend fun update(userProfile: UserProfile): UserProfile

    suspend fun findByUserId(userId: String): UserProfile?

    suspend fun deleteByUserId(userId: String): Long
}

@Repository
class UserProfileRepositoryImpl(
    private val mongoRepository: MongoUserProfileRepository,
    private val fcmDeviceRepository: FcmDeviceRepository
) : UserProfileRepository {

    override suspend fun create(userProfile: UserProfile): UserProfile {

        mongoRepository.saveUpserting(userProfile.toDocument())

        return userProfile
    }

    override suspend fun update(userProfile: UserProfile): UserProfile {

        val existingDocument = mongoRepository.findByUserId(userProfile.userId)
            ?: return create(userProfile)

        val updatedDocument = existingDocument.copy(
            avatarS3Key = userProfile.avatarS3Key
        )

        mongoRepository.saveUpserting(updatedDocument)

        return userProfile
    }

    override suspend fun findByUserId(userId: String): UserProfile? {

        val document = mongoRepository.findByUserId(userId)
            ?: return null

        val fcmDevices = fcmDeviceRepository.findByUserId(userId)

        return document.toDomain(fcmDevices)
    }

    override suspend fun deleteByUserId(userId: String): Long {
        return mongoRepository.deleteByUserId(userId)
    }

    private fun UserProfile.toDocument(): UserProfileDocument {
        return UserProfileDocument(
            id1 = userId,
            userId = userId,
            avatarS3Key = avatarS3Key
        )
    }

    private fun UserProfileDocument.toDomain(
        fcmDevices: List<FcmDevice>
    ): UserProfile {
        return UserProfile(
            userId = userId,
            avatarS3Key = avatarS3Key,
            fcmDevices = fcmDevices
        )
    }
}