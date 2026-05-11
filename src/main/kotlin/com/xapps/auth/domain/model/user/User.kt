package com.xapps.auth.domain.model.user

import com.xapps.auth.domain.exceptions.UserBannedException
import com.xapps.auth.infrastructure.security.model.UserRole
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.platform.core.time.nowInKotlinInstant
import com.xapps.platform.core.time.nowInKotlinLocalDateTime
import com.xapps.time.types.KotlinInstant
import com.xapps.time.types.KotlinLocalDateTime

data class User(
    val userId: String,
    val email: String,
    val username: String,
    val passwordHash: String,
    val role: UserRole,
    val isBanned: Boolean,
    val createdAt: KotlinInstant,
    val profile: UserProfile,
    val subscription: UserSubscription?
) {

    companion object {
        fun createNew(
            email: String,
            passwordHash: String,
            username: String,
            role: UserRole
        ) : User {
            val userId = generateUniqueId()

            return User(
                userId = userId,
                email = email,
                username = username,
                passwordHash = passwordHash,
                role = role,
                createdAt = nowInKotlinInstant(),
                profile = UserProfile.createNew(userId),
                subscription = null,
                isBanned = false
            )
        }
    }
}

fun User.ensureNotBanned(): User {
    if (isBanned) {
        throw UserBannedException()
    }
    return this
}

fun User.withIdentifyingInfo(): String = "not_yet_implemented"