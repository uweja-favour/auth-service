package com.xapps.auth.dto

import kotlinx.serialization.Serializable

/**
 * Type-safe wrapper for Raw Refresh Tokens
 * Prevents accidental mixing with access tokens at compile-time
 */
@JvmInline
@Serializable
value class RawRefreshToken(val value: String) {
    init {
        require(value.isNotBlank()) { "Raw Refresh token cannot be blank" }
    }

    override fun toString(): String = value

    companion object {
        fun fromString(token: String): RawRefreshToken = RawRefreshToken(token)
    }
}
