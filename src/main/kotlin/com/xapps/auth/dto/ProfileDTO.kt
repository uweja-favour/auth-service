package com.xapps.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    val username: String,
    val email: String
)