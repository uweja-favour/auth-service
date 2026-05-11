//package com.xapps.auth.persistence.repository
//
//import com.xapps.auth.persistence.entity.PaymentEntity
//import com.xapps.auth.domain.model.Payment
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.toList
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.jetbrains.exposed.v1.r2dbc.upsert
//import org.springframework.stereotype.Repository
//
//interface PaymentRepository {
//    suspend fun save(payment: Payment): Payment
//    suspend fun findByUserId(userId: String): List<Payment>
//}
//
//@Repository
//class PaymentRepositoryImpl(
//    private val db: R2dbcDatabase
//) : PaymentRepository {
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    override suspend fun save(payment: Payment): Payment =
//        suspendTransaction(
//            db = db
//        ) {
//            PaymentEntity.upsert {
//                it[id] = payment.id
//                it[userId] = payment.userId
//                it[amount] = payment.amount
//                it[currency] = payment.currency
//                it[platform] = payment.platform
//                it[transactionId] = payment.transactionId
//                it[paidAt] = payment.paidAt
//            }
//            payment
//        }
//
//    override suspend fun findByUserId(userId: String): List<Payment> =
//        suspendTransaction(
//            db = db
//        ) {
//            PaymentEntity.selectAll()
//                .where { PaymentEntity.userId eq userId }
//                .map {
//                    Payment(
//                        id = it[PaymentEntity.id],
//                        userId = it[PaymentEntity.userId],
//                        amount = it[PaymentEntity.amount],
//                        currency = it[PaymentEntity.currency],
//                        platform = it[PaymentEntity.platform],
//                        transactionId = it[PaymentEntity.transactionId],
//                        paidAt = it[PaymentEntity.paidAt]
//                    )
//                }
//                .toList()
//        }
//}