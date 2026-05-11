package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.NotificationDocument
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import kotlin.jvm.java

interface NotificationRepositoryCustom {

    suspend fun markAllAsRead(
        userId: String,
        ids: List<String>
    ): Long

    suspend fun markAsRead(
        userId: String,
        id: String
    ): Long
}

@Repository
class NotificationRepositoryCustomImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : NotificationRepositoryCustom {

    override suspend fun markAllAsRead(
        userId: String,
        ids: List<String>
    ): Long {

        val query = Query().apply {
            addCriteria(
                Criteria.where("userId").`is`(userId)
                    .and("_id").`in`(ids)
                    .and("read").`is`(false)
            )
        }

        val update = Update().set("read", true)

        return mongoTemplate
            .updateMulti(query, update, NotificationDocument::class.java)
            .awaitSingle()
            .modifiedCount
    }

    override suspend fun markAsRead(
        userId: String,
        id: String
    ): Long {

        val query = Query().apply {
            addCriteria(
                Criteria.where("userId").`is`(userId)
                    .and("_id").`is`(id)
                    .and("read").`is`(false)
            )
        }

        val update = Update().set("read", true)

        return mongoTemplate
            .updateFirst(query, update, NotificationDocument::class.java)
            .awaitSingle()
            .modifiedCount
    }
}