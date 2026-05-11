package com.xapps.auth.domain.model

import com.xapps.platform.core.time.nowInKotlinInstant
import com.xapps.time.types.KotlinInstant
import kotlin.time.Duration.Companion.days

data class MonthlyReset(
    val id: String = "singleton",
    val lastReset: KotlinInstant = nowInKotlinInstant() - 1100.days
)