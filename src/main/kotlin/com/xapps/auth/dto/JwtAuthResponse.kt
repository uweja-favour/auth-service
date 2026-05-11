package com.xapps.auth.dto

import kotlinx.serialization.Serializable

/**
 * Type-safe JWT response that prevents token swapping at compile-time
 */
@Serializable
data class JwtAuthResponse(
    val accessToken: RawAccessToken,
    val refreshToken: RawRefreshToken
) {
    // Convenience for logging - masks tokens
    override fun toString(): String =
        "JwtAuthResponse(accessToken=***${accessToken.value.takeLast(6)}, " +
                "refreshToken=***${refreshToken.value.takeLast(6)})"
}