package com.xapps.auth.domain.model

import com.xapps.time.types.KotlinInstant

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType = NotificationType.SYSTEM,
    val instant: KotlinInstant,
    val read: Boolean = false
)