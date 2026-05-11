package com.xapps.auth.persistence.repository.user.impl

import com.xapps.auth.domain.model.user.SubscriptionPlan
import com.xapps.auth.domain.model.user.SubscriptionPlatform
import com.xapps.auth.domain.model.user.UserSubscription
import com.xapps.auth.persistence.entity.user.UserSubscriptionDocument
import com.xapps.auth.persistence.repository.user.MongoUserSubscriptionRepository
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface UserSubscriptionRepository {

    suspend fun insert(subscription: UserSubscription): UserSubscription

    suspend fun update(subscription: UserSubscription): UserSubscription

    suspend fun findByUserId(userId: String): UserSubscription?

    suspend fun deleteByUserId(userId: String): Long
}

@Repository
class UserSubscriptionRepositoryImpl(
    private val mongoRepository: MongoUserSubscriptionRepository
) : UserSubscriptionRepository {

    /**
     * Used to create a new subscription for a user who never had a subscription before.
     * To renew a user subscription, see renewSubscription method.
     */
    override suspend fun insert(
        subscription: UserSubscription
    ): UserSubscription {

        val existingSubscription = mongoRepository.findByUserId(subscription.userId)

        if (existingSubscription != null) {
            throw RuntimeException(
                "User with id: ${subscription.userId} already has a subscription"
            )
        }

        mongoRepository.saveUpserting(subscription.toDocument())

        return subscription
    }

    override suspend fun update(
        subscription: UserSubscription
    ): UserSubscription {

        val existingSubscription = mongoRepository.findByUserId(subscription.userId)
            ?: throw RuntimeException(
                "User with id: ${subscription.userId} does not have a subscription"
            )

        val updatedDocument = existingSubscription.copy(
            planCode = subscription.plan.code,
            platformCode = subscription.platform.code,
            startAt = subscription.startAt,
            endAt = subscription.endAt,
            lastRenewal = subscription.lastRenewal
        )

        mongoRepository.saveUpserting(updatedDocument)

        return subscription
    }

    override suspend fun findByUserId(
        userId: String
    ): UserSubscription? {

        return mongoRepository.findByUserId(userId)
            ?.toDomain()
    }

    override suspend fun deleteByUserId(
        userId: String
    ): Long {

        return mongoRepository.deleteByUserId(userId)
    }

    private fun UserSubscription.toDocument(): UserSubscriptionDocument {
        return UserSubscriptionDocument(
            id1 = id,
            userId = userId,
            planCode = plan.code,
            platformCode = platform.code,
            startAt = startAt,
            endAt = endAt,
            lastRenewal = lastRenewal
        )
    }

    private fun UserSubscriptionDocument.toDomain(): UserSubscription {
        return UserSubscription(
            id = id1,
            userId = userId,
            plan = SubscriptionPlan.fromCode(planCode),
            platform = SubscriptionPlatform.fromCode(platformCode),
            startAt = startAt,
            endAt = endAt,
            lastRenewal = lastRenewal
        )
    }
}