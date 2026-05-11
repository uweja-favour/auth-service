package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.domain.model.UsedGooglePlaySubscriptionPurchaseToken
import com.xapps.auth.persistence.entity.UsedGooglePlaySubscriptionPurchaseTokenDocument
import com.xapps.auth.persistence.repository.MongoUsedGooglePlayPurchaseTokenRepository
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface UsedGooglePlayPurchaseTokenRepository {

    suspend fun save(
        token: UsedGooglePlaySubscriptionPurchaseToken
    ): UsedGooglePlaySubscriptionPurchaseToken

    suspend fun deactivateAllForUser(userId: String): Long

    suspend fun findAllForUser(
        userId: String
    ): List<UsedGooglePlaySubscriptionPurchaseToken>

    suspend fun findAllByIsActive(
        userId: String,
        isActive: Boolean
    ): List<UsedGooglePlaySubscriptionPurchaseToken>
}

@Repository
class UsedGooglePlayPurchaseTokenRepositoryImpl(
    private val mongoRepository: MongoUsedGooglePlayPurchaseTokenRepository
) : UsedGooglePlayPurchaseTokenRepository {

    override suspend fun save(
        token: UsedGooglePlaySubscriptionPurchaseToken
    ): UsedGooglePlaySubscriptionPurchaseToken {

        mongoRepository.saveUpserting(token.toDocument())
        return token
    }

    override suspend fun deactivateAllForUser(
        userId: String
    ): Long {

        return mongoRepository.deactivateAllByUserId(userId)
    }

    override suspend fun findAllForUser(
        userId: String
    ): List<UsedGooglePlaySubscriptionPurchaseToken> {

        return mongoRepository.findAllByUserId(userId)
            .map { it.toDomain() }
    }

    override suspend fun findAllByIsActive(
        userId: String,
        isActive: Boolean
    ): List<UsedGooglePlaySubscriptionPurchaseToken> {

        return mongoRepository.findAllByUserIdAndIsActive(
            userId = userId,
            isActive = isActive
        ).map { it.toDomain() }
    }

    private fun UsedGooglePlaySubscriptionPurchaseToken.toDocument():
            UsedGooglePlaySubscriptionPurchaseTokenDocument {

        return UsedGooglePlaySubscriptionPurchaseTokenDocument(
            purchaseToken = purchaseToken,
            subscriptionId = subscriptionId,
            packageName = packageName,
            userId = userId,
            lastExpiryMillis = lastExpiryMillis,
            isActive = isActive
        )
    }

    private fun UsedGooglePlaySubscriptionPurchaseTokenDocument.toDomain():
        UsedGooglePlaySubscriptionPurchaseToken {

        return UsedGooglePlaySubscriptionPurchaseToken(
            purchaseToken = purchaseToken,
            subscriptionId = subscriptionId,
            packageName = packageName,
            userId = userId,
            lastExpiryMillis = lastExpiryMillis,
            isActive = isActive
        )
    }
}