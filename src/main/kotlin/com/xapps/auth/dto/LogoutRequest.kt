package com.xapps.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: RawRefreshToken,
)
