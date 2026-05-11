package com.xapps.auth.persistence.repository.user.impl

import com.xapps.auth.domain.model.user.DevicePlatform
import com.xapps.auth.domain.model.user.FcmDevice
import com.xapps.auth.persistence.repository.user.MongoFcmDeviceRepository
import com.xapps.auth.persistence.entity.user.FcmDeviceDocument
import com.xapps.auth.persistence.saveUpserting
import com.xapps.time.types.KotlinInstant
import com.xapps.time.types.KotlinLocalDateTime
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.springframework.stereotype.Repository

interface FcmDeviceRepository {

    suspend fun registerDevice(device: FcmDevice): FcmDevice

    suspend fun findByUserId(userId: String): List<FcmDevice>

    suspend fun deleteByToken(token: String): Long
}

@Repository
class FcmDeviceRepositoryImpl(
    private val mongoRepository: MongoFcmDeviceRepository
) : FcmDeviceRepository {

    override suspend fun registerDevice(device: FcmDevice): FcmDevice {

        val existingDevice = mongoRepository.findByToken(device.token)

        val document = if (existingDevice != null) {
            existingDevice.copy(
                userId = device.userId,
                platformCode = device.platform.code,
                deviceModel = device.deviceModel,
                lastActiveAt = device.lastActiveAt,
                updatedAt = device.updatedAt
            )
        } else {
            device.toDocument()
        }

        mongoRepository.saveUpserting(document)

        return device
    }

    override suspend fun findByUserId(userId: String): List<FcmDevice> {
        return mongoRepository
            .findAllByUserId(userId)
            .map { it.toDomain() }
            .toList()
    }

    override suspend fun deleteByToken(token: String): Long {
        return mongoRepository.deleteByToken(token)
    }

    private fun FcmDevice.toDocument(): FcmDeviceDocument {
        return FcmDeviceDocument(
            id1 = fcmDeviceId,
            userId = userId,
            token = token,
            platformCode = platform.code,
            deviceModel = deviceModel,
            lastActiveAt = lastActiveAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun FcmDeviceDocument.toDomain(): FcmDevice {
        return FcmDevice(
            fcmDeviceId = id1,
            userId = userId,
            token = token,
            platform = DevicePlatform.fromCode(platformCode) ?: error("Invalid platform code: $platformCode"),
            deviceModel = deviceModel,
            lastActiveAt = lastActiveAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun KotlinLocalDateTime.toKotlinInstant(): KotlinInstant {
        return toInstant(TimeZone.UTC)
    }
}