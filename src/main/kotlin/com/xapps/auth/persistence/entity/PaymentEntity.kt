package com.xapps.auth.persistence.entity

import com.xapps.auth.domain.model.user.SubscriptionPlatformCode
import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object PaymentEntity : Table(PaymentTable.TABLE_NAME) {
//    val id = varchar(PaymentTable.ID, 100)
//        .clientDefault { generateUniqueId() }
//    val userId = varchar(PaymentTable.USER_ID, 255)
//    val amount = double(PaymentTable.AMOUNT)
//    val currency = varchar(PaymentTable.CURRENCY, 100).default("USD")
//    val platform = enumerationByName(PaymentTable.PLATFORM, 100, SubscriptionPlatform::class)
//    val transactionId = varchar(PaymentTable.TRANSACTION_ID, 255).nullable()
//    val paidAt = datetime(PaymentTable.PAID_AT).default(nowInKotlinLocalDateTime())
//
//    override val primaryKey: PrimaryKey = PrimaryKey(id)
//}
//
//object PaymentTable {
//    const val TABLE_NAME = "payment"
//    const val ID = "id"
//    const val USER_ID = "user_id"
//    const val AMOUNT = "amount"
//    const val CURRENCY = "currency"
//    const val PLATFORM = "platform"
//    const val TRANSACTION_ID = "transaction_id"
//    const val PAID_AT = "paid_at"
//}


@Document("payments")
data class PaymentDocument(
    @Id
    val id1: String,

    val userId: String,
    val amount: Double,
    val currency: String,
    val subscriptionPlatformCode: SubscriptionPlatformCode,
    val transactionId: String?,
    val paidAt: KotlinInstant
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}

