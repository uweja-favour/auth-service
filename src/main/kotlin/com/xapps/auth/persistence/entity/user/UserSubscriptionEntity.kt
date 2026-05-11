package com.xapps.auth.persistence.entity.user

import com.xapps.auth.domain.model.user.SubscriptionPlanCode
import com.xapps.auth.domain.model.user.SubscriptionPlatformCode
import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object UserSubscriptionEntity : Table(UserSubscriptionTable.TABLE_NAME) {
//    val id = varchar(UserSubscriptionTable.ID, 100)
//    val userId = varchar(UserSubscriptionTable.USER_ID, 100)
//    val plan = enumerationByName(UserSubscriptionTable.SUBSCRIPTION_PLAN, 100, SubscriptionPlan::class)
//    val platform = enumerationByName(UserSubscriptionTable.PLATFORM, 100, SubscriptionPlatform::class)
//    val startAt = datetime(UserSubscriptionTable.START_AT)
//    val endAt = datetime(UserSubscriptionTable.END_AT).nullable()
//    val lastRenewal = datetime(UserSubscriptionTable.LAST_RENEWAL).nullable()
//
//    override val primaryKey: PrimaryKey = PrimaryKey(id)
//}
//
//object UserSubscriptionTable {
//    const val TABLE_NAME = "user_subscription"
//    const val ID = "id"
//    const val USER_ID = "user_id"
//    const val PLATFORM = "platform"
//    const val SUBSCRIPTION_PLAN = "subscription_plan"
//    const val START_AT = "start_at"
//    const val END_AT = "end_at"
//    const val LAST_RENEWAL = "last_renewal"
//}
//
//
//sealed class SubscriptionStatusResult {
//    data object Active : SubscriptionStatusResult()
//    data object Inactive : SubscriptionStatusResult()
//    data class Error(val message: String) : SubscriptionStatusResult()
//}


@Document("user_subscriptions")
data class UserSubscriptionDocument(
    @Id
    val id1: String,

    val userId: String,
    val planCode: SubscriptionPlanCode,
    val platformCode: SubscriptionPlatformCode,
    val startAt: KotlinInstant,
    val endAt: KotlinInstant?,
    val lastRenewal: KotlinInstant?
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}