package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.PaymentDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoPaymentRepository :
    CoroutineCrudRepository<PaymentDocument, String> {

    suspend fun findAllByUserId(userId: String): List<PaymentDocument>
}