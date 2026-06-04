package com.xapps.auth.dto

import com.xapps.model.FileData
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val profilePhoto: FileData?
)