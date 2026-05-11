package com.xapps.auth.domain.model

import com.xapps.auth.domain.model.user.SubscriptionPlatform
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.time.types.KotlinInstant
import com.xapps.time.types.KotlinLocalDateTime

data class Payment(
    val id: String = generateUniqueId(),
    val userId: String,
    val amount: Double,
    val currency: String = "USD",
    val subscriptionPlatform: SubscriptionPlatform,
    val transactionId: String? = null,
    val paidAt: KotlinInstant
)