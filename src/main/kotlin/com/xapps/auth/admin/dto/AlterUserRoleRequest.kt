package com.xapps.auth.admin.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AlterUserRoleRequest(
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "Role must be specified")
    val newRole: String
)
