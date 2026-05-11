@file:OptIn(ExperimentalTime::class)

package com.xapps.auth.infrastructure.security.model

import com.xapps.auth.domain.exceptions.RefreshTokenExpiredException
import com.xapps.auth.domain.exceptions.RefreshTokenRevokedException
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.platform.core.time.nowInKotlinInstant
import com.xapps.time.types.KotlinInstant
import com.xapps.time.types.KotlinLocalDateTime
import kotlin.time.ExperimentalTime

data class RefreshToken(
    val id: String = generateUniqueId(),
    val userId: String,
    val jti: String,
    val tokenHash: String,
    val createdAt: KotlinInstant,
    val lastUsedAt: KotlinInstant? = null,
    val deviceIp: String? = null,
    val userAgent: String? = null,
    val expiryAt: KotlinInstant,
    val revoked: Boolean = false
)

fun RefreshToken.ensureNotExpired(now: KotlinInstant): RefreshToken {
    if (expiryAt <= now) {
        throw RefreshTokenExpiredException()
    }
    return this
}

fun RefreshToken.ensureNotRevoked(): RefreshToken {
    if (revoked) {
        throw RefreshTokenRevokedException()
    }
    return this
}

