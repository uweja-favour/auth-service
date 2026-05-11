package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.UsedGooglePlaySubscriptionPurchaseTokenDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoUsedGooglePlayPurchaseTokenRepository :
    CoroutineCrudRepository<UsedGooglePlaySubscriptionPurchaseTokenDocument, String>,
        MongoUsedGooglePlayPurchaseTokenCustomRepository {

    suspend fun findAllByUserId(userId: String):
            List<UsedGooglePlaySubscriptionPurchaseTokenDocument>

    suspend fun findAllByUserIdAndIsActive(
        userId: String,
        isActive: Boolean
    ): List<UsedGooglePlaySubscriptionPurchaseTokenDocument>
}