package com.xapps.auth.persistence.entity

import com.xapps.auth.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//object MonthlyResetEntity : Table(MonthlyResetTable.TABLE_NAME) {
//    val id = varchar(MonthlyResetTable.ID, 100).default("singleton")
//    val lastReset = date(MonthlyResetTable.LAST_RESET).default(
//        com.xapps.time.types.KotlinLocalDate(
//            2023,
//            1,
//            1
//        )
//    )
//    override val primaryKey = PrimaryKey(id)
//}
//
//object MonthlyResetTable {
//    const val TABLE_NAME = "monthly_reset"
//    const val ID = "id"
//    const val LAST_RESET = "last_reset"
//}

@Document("monthly_resets")
data class MonthlyResetDocument(
    @Id
    val id1: String,

    val lastReset: KotlinInstant
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}