@file:OptIn(ExperimentalTime::class)

package com.xapps.auth.persistence.entity

import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import kotlin.time.ExperimentalTime

//object RefreshTokenEntity : Table(RefreshTokenTable.TABLE_NAME) {
//    val id = varchar(RefreshTokenTable.ID, 36)
//    val userId = varchar(RefreshTokenTable.USER_ID, 255)
//    val jti = varchar(RefreshTokenTable.JTI, 255).uniqueIndex()
//    val tokenHash = varchar(RefreshTokenTable.TOKEN_HASH, 64).uniqueIndex()
//    val createdAt = datetime(RefreshTokenTable.CREATED_AT)
//    val lastUsedAt = datetime(RefreshTokenTable.LAST_USED_AT).nullable()
//    val deviceIp = varchar(RefreshTokenTable.DEVICE_IP, 45).nullable()
//    val userAgent = varchar(RefreshTokenTable.USER_AGENT, 1024).nullable()
//    val expiryAt = datetime(RefreshTokenTable.EXPIRY_AT)
//    val revoked = bool(RefreshTokenTable.REVOKED).default(false)
//
//    override val primaryKey = PrimaryKey(id)
//}
//
//object RefreshTokenTable {
//    const val TABLE_NAME = "refresh_tokens"
//    const val ID = "id"
//    const val USER_ID = "user_id"
//    const val JTI = "jti"
//    const val TOKEN_HASH = "token_hash"
//    const val CREATED_AT = "created_at"
//    const val LAST_USED_AT = "last_used_at"
//    const val DEVICE_IP = "device_ip"
//    const val USER_AGENT = "user_agent"
//    const val EXPIRY_AT = "expiry_date"
//    const val REVOKED = "revoked"
//}

@Document("refresh_tokens")
data class RefreshTokenDocument(
    @Id
    val id1: String,

    val userId: String,
    val jti: String,
    val tokenHash: String,
    val createdAt: KotlinInstant,
    val lastUsedAt: KotlinInstant?,
    val deviceIp: String?,
    val userAgent: String?,
    val expiryAt: KotlinInstant,
    val revoked: Boolean
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}