package com.xapps.auth.persistence.entity

import com.xapps.auth.persistence.BasePersistableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

//object UsedGooglePlaySubscriptionPurchaseTokenEntity : Table(UsedGooglePlayPurchaseTokenTable.TABLE_NAME) {
//    val id = varchar(UsedGooglePlayPurchaseTokenTable.ID, 500)
//        .clientDefault { generateUniqueId() }
//    val purchaseToken = varchar(UsedGooglePlayPurchaseTokenTable.PURCHASE_TOKEN, 500)
//    val subscriptionId = varchar(UsedGooglePlayPurchaseTokenTable.SUBSCRIPTION_ID, 500)
//    val packageName = varchar(UsedGooglePlayPurchaseTokenTable.PACKAGE_NAME, 500)
//    val userId = varchar(UsedGooglePlayPurchaseTokenTable.USER_ID, 200)
//    val lastExpiryMillis = long(UsedGooglePlayPurchaseTokenTable.LAST_EXPIRY_MILLIS)
//    val isActive = bool(UsedGooglePlayPurchaseTokenTable.IS_ACTIVE)
//
//    override val primaryKey = PrimaryKey(id)
//}
//
//object UsedGooglePlayPurchaseTokenTable {
//    const val TABLE_NAME = "used_google_play_subscription_purchase_token"
//    const val ID = "id"
//    const val PURCHASE_TOKEN = "purchase_token"
//    const val SUBSCRIPTION_ID = "subscription_id"
//    const val PACKAGE_NAME = "package_name"
//    const val USER_ID = "user_id"
//    const val LAST_EXPIRY_MILLIS = "last_expiry_millis"
//    const val IS_ACTIVE = "is_active"
//}

@Document("used_google_play_subscription_purchase_tokens")
data class UsedGooglePlaySubscriptionPurchaseTokenDocument(
    @Id
    val purchaseToken: String,
    val subscriptionId: String,
    val packageName: String,

    @Indexed
    val userId: String,

    val lastExpiryMillis: Long,
    val isActive: Boolean
) : BasePersistableEntity() {

    override fun getTheId(): String = purchaseToken
}