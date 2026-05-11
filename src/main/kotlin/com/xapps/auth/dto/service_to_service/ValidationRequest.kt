@file:OptIn(ExperimentalTime::class)

package com.xapps.auth.dto.service_to_service

import com.xapps.auth.dto.RawAccessToken
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

data class JwtClaims(
    val userId: String,
    val email: String?,
    val roles: List<String>,
    val expiryAt: Long
)

@Serializable
data class ValidationRequest(val rawAccessToken: RawAccessToken)

@Serializable
data class ValidationResponse(val userId: String?, val email: String?, val roles: List<String>, val expiryAt: Long?) {
    val isValid: Boolean
        get() = !userId.isNullOrBlank() &&
                !email.isNullOrBlank() &&
                expiryAt != null

    companion object {
        fun invalid() = ValidationResponse(
            userId = null,
            email = null,
            roles = emptyList(),
            expiryAt = null
        )

        fun valid(jwtClaim: JwtClaims) = ValidationResponse(
            userId = jwtClaim.userId,
            email =  jwtClaim.email,
            roles = jwtClaim.roles,
            expiryAt = jwtClaim.expiryAt
        )
    }
}