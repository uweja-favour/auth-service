package com.xapps.auth.persistence.entity.user

import com.xapps.auth.infrastructure.security.model.UserRole
import com.xapps.auth.infrastructure.security.model.UserRoleCode
import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object UserEntity : Table(UserTable.TABLE_NAME) {
//    val userId = varchar(UserTable.USER_ID, 200)
//    val email = varchar(UserTable.EMAIL, 255).uniqueIndex()
//    val username = varchar(UserTable.USERNAME, 255)
//    val passwordHash = varchar(UserTable.PASSWORD_HASH, 255)
//    val role = enumerationByName(UserTable.ROLE, 50, UserRole::class)
//    val isBanned = bool(UserTable.IS_BANNED).default(false)
//    val createdAt = datetime(UserTable.CREATED_AT)
//
//    override val primaryKey = PrimaryKey(userId)
//}
//
//object UserTable {
//    const val TABLE_NAME = "users"
//    const val USER_ID = "user_id"
//    const val EMAIL = "email"
//    const val USERNAME = "username"
//    const val PASSWORD_HASH = "password_hash"
//    const val ROLE = "role"
//    const val IS_BANNED = "is_banned"
//    const val CREATED_AT = "created_at"
//}

@Document("users")
data class UserDocument (
    @Id
    val userId: String,

    val email: String,
    val username: String,
    val passwordHash: String,
    val roleCode: UserRoleCode,
    val isBanned: Boolean,
    val createdAt: KotlinInstant
) : BasePersistableEntity() {

    override fun getTheId() = userId

}