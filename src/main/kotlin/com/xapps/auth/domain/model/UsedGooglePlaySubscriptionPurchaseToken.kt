package com.xapps.auth.domain.model

data class UsedGooglePlaySubscriptionPurchaseToken(
    val purchaseToken: String,
    val subscriptionId: String,
    val packageName: String,
    val userId: String,
    val lastExpiryMillis: Long,
    val isActive: Boolean
)