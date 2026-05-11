package com.xapps.auth.dto

import kotlinx.serialization.Serializable

/**
 * Type-safe wrapper for Raw Access Tokens
 * Prevents accidental mixing with refresh tokens at compile-time
 */
@JvmInline
@Serializable
value class RawAccessToken(val value: String) {
    init {
        require(value.isNotBlank()) { "Raw Access token cannot be blank" }
    }

    override fun toString(): String = value

    companion object {
        fun fromString(token: String): RawAccessToken = RawAccessToken(token)
    }
}
