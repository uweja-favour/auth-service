package com.xapps.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @field:NotBlank val oldPassword: String,
    @field:Size(min = 8, message = "New password must be at least 8 characters")
    val newPassword: String
)