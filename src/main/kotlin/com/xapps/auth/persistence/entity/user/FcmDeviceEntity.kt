package com.xapps.auth.persistence.entity.user

import com.xapps.auth.domain.model.user.DevicePlatformCode
import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object FcmDeviceEntity : Table("fcm_devices") {
//    val id = varchar("id", 200)
//        .clientDefault { generateUniqueId() }
//    val userId = varchar("user_id", 200)
//        .references(UserEntity.userId, onDelete = ReferenceOption.CASCADE)
//    val token = varchar("token", 500).uniqueIndex()
//    val platform = enumerationByName("platform", 50, DevicePlatform::class)
//    val deviceModel = varchar("device_model", 255).nullable()
//    val lastActiveAt = datetime("last_active_at").nullable()
//    val createdAt = datetime("created_at")
//    val updatedAt = datetime("updated_at").nullable()
//
//    override val primaryKey = PrimaryKey(id)
//}

@Document("fcm_devices")
data class FcmDeviceDocument(
    @Id
    val id1: String,

    val userId: String,
    val token: String,
    val platformCode: DevicePlatformCode,
    val deviceModel: String?,
    val lastActiveAt: KotlinInstant?,
    val createdAt: KotlinInstant,
    val updatedAt: KotlinInstant?
) : BasePersistableEntity() {

    override fun getTheId() = id1
}