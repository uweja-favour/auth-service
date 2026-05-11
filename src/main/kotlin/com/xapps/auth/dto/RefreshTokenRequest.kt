package com.xapps.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: RawRefreshToken)
