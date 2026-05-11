package com.xapps.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFcmTokenRequestDto(
    val fcmToken: String
)