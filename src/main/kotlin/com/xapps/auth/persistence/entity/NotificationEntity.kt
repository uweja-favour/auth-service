package com.xapps.auth.persistence.entity

import com.xapps.auth.domain.model.NotificationTypeCode
import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object NotificationEntity : Table(NotificationTable.TABLE_NAME) {
//    val id = long(NotificationTable.ID).autoIncrement()
//    val userId = varchar(NotificationTable.USER_ID, 36)
//    val title = varchar(NotificationTable.TITLE, 255)
//    val message = varchar(NotificationTable.MESSAGE, 1024)
//    val type = enumerationByName(NotificationTable.TYPE, 50, NotificationType::class)
//    val datetime = datetime(NotificationTable.DATETIME)  // ✅ changed from timestamp()
//    val read = bool(NotificationTable.READ).default(false)
//
//    override val primaryKey: PrimaryKey = PrimaryKey(id)
//}
//
//object NotificationTable {
//    const val TABLE_NAME = "notifications"
//    const val ID = "id"
//    const val USER_ID = "user_id"
//    const val TITLE = "title"
//    const val MESSAGE = "message"
//    const val TYPE = "type"
//    const val DATETIME = "datetime"
//    const val READ = "is_read"
//}

@Document("notifications")
data class NotificationDocument(
    @Id
    val id1: String,

    val userId: String,
    val title: String,
    val message: String,
    val notificationTypeCode: NotificationTypeCode,
    val instant: KotlinInstant,
    val read: Boolean
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}