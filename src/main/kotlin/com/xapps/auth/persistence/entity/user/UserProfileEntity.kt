package com.xapps.auth.persistence.entity.user

import com.xapps.auth.persistence.BasePersistableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object UserProfileEntity : Table("user_profiles") {
//    val id = varchar(UserProfileTable.ID, 100)
//        .clientDefault { generateUniqueId() }
//    val userId = varchar(UserProfileTable.USER_ID, 200)
//        .references(UserEntity.userId, onDelete = ReferenceOption.CASCADE)
//    val avatarS3Key = varchar(UserProfileTable.AVATAR_S3_KEY, 255).nullable()
//
//    override val primaryKey = PrimaryKey(id)
//}
//
//object UserProfileTable {
//    const val ID = "id"
//    const val USER_ID = "user_id"
//    const val AVATAR_S3_KEY = "avatar_s3_key"
//}

@Document("user_profiles")
data class UserProfileDocument(
    @Id
    val id1: String,

    val userId: String,
    val avatarS3Key: String?
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}