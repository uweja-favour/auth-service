package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.domain.model.Payment
import com.xapps.auth.domain.model.user.SubscriptionPlatform
import com.xapps.auth.persistence.entity.PaymentDocument
import com.xapps.auth.persistence.repository.MongoPaymentRepository
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface PaymentRepository {

    suspend fun save(payment: Payment): Payment

    suspend fun findByUserId(userId: String): List<Payment>
}

@Repository
class PaymentRepositoryImpl(
    private val mongoRepository: MongoPaymentRepository
) : PaymentRepository {

    override suspend fun save(payment: Payment): Payment {
        mongoRepository.saveUpserting(payment.toDocument())
        return payment
    }

    override suspend fun findByUserId(userId: String): List<Payment> {
        return mongoRepository
            .findAllByUserId(userId)
            .map { it.toDomain() }
    }

    private fun Payment.toDocument(): PaymentDocument {
        return PaymentDocument(
            id1 = id,
            userId = userId,
            amount = amount,
            currency = currency,
            subscriptionPlatformCode = subscriptionPlatform.code,
            transactionId = transactionId,
            paidAt = paidAt
        )
    }

    private fun PaymentDocument.toDomain(): Payment {
        return Payment(
            id = id1,
            userId = userId,
            amount = amount,
            currency = currency,
            subscriptionPlatform = SubscriptionPlatform.fromCode(subscriptionPlatformCode),
            transactionId = transactionId,
            paidAt = paidAt
        )
    }
}