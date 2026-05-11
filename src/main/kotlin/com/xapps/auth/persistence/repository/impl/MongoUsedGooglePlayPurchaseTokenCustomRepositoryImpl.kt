package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.persistence.entity.UsedGooglePlaySubscriptionPurchaseTokenDocument
import com.xapps.auth.persistence.repository.MongoUsedGooglePlayPurchaseTokenCustomRepository
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class MongoUsedGooglePlayPurchaseTokenCustomRepositoryImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : MongoUsedGooglePlayPurchaseTokenCustomRepository {

    override suspend fun deactivateAllByUserId(userId: String): Long {

        val query = Query(
            Criteria.where("userId").`is`(userId)
        )

        val update = Update().set("isActive", false)

        return mongoTemplate
            .updateMulti(query, update, UsedGooglePlaySubscriptionPurchaseTokenDocument::class.java)
            .awaitSingle()
            .modifiedCount
    }
}