package com.xapps.auth.domain.model.user

import com.xapps.time.types.KotlinInstant

data class UserSubscription(
    val id: String,
    val userId: String,
    val plan: SubscriptionPlan,
    val platform: SubscriptionPlatform,
    val startAt: KotlinInstant,
    val endAt: KotlinInstant?,
    val lastRenewal: KotlinInstant?
)

@JvmInline
value class SubscriptionPlanCode(val value: String)

enum class SubscriptionPlan(val code: SubscriptionPlanCode, val monthlyQuizTokens: Int) {
    FREE(SubscriptionPlanCode("free"),15),
    PRO(SubscriptionPlanCode("pro"),30),
    PREMIUM(SubscriptionPlanCode("premium"), 500),
    NONE(SubscriptionPlanCode("none"), 0);

    companion object {
        private val byCodes = entries.associateBy { it.code }

        fun fromCode(code: SubscriptionPlanCode): SubscriptionPlan =
            byCodes[code] ?: throw IllegalArgumentException("Unknown SubscriptionPlatformCode $code")
    }
}

@JvmInline
value class SubscriptionPlatformCode(val value: String)

enum class SubscriptionPlatform(val code: SubscriptionPlatformCode) {
    STRIPE(SubscriptionPlatformCode("stripe")),
    GOOGLE_PLAY(SubscriptionPlatformCode("google_play"));

    companion object {
        private val byCodes = entries.associateBy { it.code }

        fun fromCode(code: SubscriptionPlatformCode): SubscriptionPlatform =
            byCodes[code] ?: throw IllegalArgumentException("Unknown SubscriptionPlatformCode $code")
    }
}