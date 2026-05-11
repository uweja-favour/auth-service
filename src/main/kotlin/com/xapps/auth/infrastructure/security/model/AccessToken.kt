@file:OptIn(ExperimentalTime::class)

package com.xapps.auth.infrastructure.security.model

import com.xapps.platform.core.time.nowInKotlinInstant
import com.xapps.time.types.KotlinInstant
import kotlin.time.ExperimentalTime

data class AccessToken(
    val jti: String,
    val userId: String,
    val createdAt: KotlinInstant,
    val expiryAt: KotlinInstant,
    val revoked: Boolean = false
) {
    companion object {
        fun new(
            jti: String,
            userId: String,
            expiryAt: KotlinInstant,
            now: KotlinInstant
        ) = AccessToken(
            jti = jti,
            userId = userId,
            createdAt = now,
            expiryAt = expiryAt,
            revoked = false
        )
    }
}
