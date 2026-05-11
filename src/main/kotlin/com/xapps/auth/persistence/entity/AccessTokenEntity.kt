package com.xapps.auth.persistence.entity

import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object AccessTokenEntity : Table(AccessTokenTable.TABLE_NAME) {
//    val jti = varchar(AccessTokenTable.JTI, 500)
//    val userId = varchar(AccessTokenTable.USER_ID, 500)
//    val createdAt = datetime(AccessTokenTable.CREATED_AT)   // ✅ changed from timestamp()
//    val expiryAt = datetime(AccessTokenTable.EXPIRY_AT)     // ✅ changed from timestamp()
//    val revoked = bool(AccessTokenTable.REVOKED).default(false)
//    val list = array<String>("")
//
//    override val primaryKey = PrimaryKey(jti)
//}
//
//object AccessTokenTable {
//    const val TABLE_NAME = "access_token"
//    const val JTI = "jti"
//    const val USER_ID = "user_id"
//    const val CREATED_AT = "created_at"
//    const val EXPIRY_AT = "expiry_at"
//    const val REVOKED = "revoked"
//}

@Document("access_token")
data class AccessTokenDocument(
    @Id
    val jti: String,

    val userId: String,
    val createdAt: KotlinInstant,
    val expiryAt: KotlinInstant,
    val revoked: Boolean
) : BasePersistableEntity() {

    override fun getTheId(): String = jti
}