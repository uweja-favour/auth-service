package com.xapps.auth.domain.model.user

import com.xapps.time.types.KotlinInstant
import com.xapps.time.types.KotlinLocalDateTime

data class UserProfile(
    val userId: String,
    val avatarS3Key: String? = null,
    val fcmDevices: List<FcmDevice> = emptyList()
) {
    companion object {
        fun createNew(userId: String): UserProfile {
            return UserProfile(
                userId = userId
            )
        }
    }
}

data class FcmDevice(
    val fcmDeviceId: String,
    val userId: String,
    val token: String,
    val platform: DevicePlatform,
    val deviceModel: String?,
    val lastActiveAt: KotlinInstant?,
    val createdAt: KotlinInstant,
    val updatedAt: KotlinInstant?,
)

@JvmInline
value class DevicePlatformCode(val value: String)

enum class DevicePlatform(val code: DevicePlatformCode) {
    ANDROID(DevicePlatformCode("android")),
    IOS(DevicePlatformCode("ios")),
    WEB(DevicePlatformCode("web")),
    DESKTOP(DevicePlatformCode("desktop"));

    companion object {
        private val byCodes = entries.associateBy { it.code }

        fun fromCode(code: DevicePlatformCode): DevicePlatform? =
            byCodes[code]
    }
}
