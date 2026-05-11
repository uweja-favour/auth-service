//package com.xapps.auth.persistence.repository
//
//import com.xapps.auth.persistence.entity.MonthlyResetEntity
//import com.xapps.auth.domain.model.MonthlyReset
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.firstOrNull
//import org.jetbrains.exposed.v1.core.eq
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.insert
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.springframework.stereotype.Repository
//
//interface MonthlyResetRepository {
//    suspend fun get(): MonthlyReset?
//    suspend fun insert(reset: MonthlyReset): MonthlyReset
//    suspend fun update(reset: MonthlyReset): MonthlyReset
//}
//
//@Repository
//class MonthlyResetRepositoryImpl(private val db: R2dbcDatabase) : MonthlyResetRepository {
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    override suspend fun get(): MonthlyReset? =
//        suspendTransaction(
//            db = db
//        ) {
//            MonthlyResetEntity.selectAll().firstOrNull()?.let {
//                MonthlyReset(it[MonthlyResetEntity.id], it[MonthlyResetEntity.lastReset])
//            }
//        }
//
//    override suspend fun insert(reset: MonthlyReset): MonthlyReset =
//        suspendTransaction(
//            db = db
//        ) {
//            MonthlyResetEntity.insert {
//                it[MonthlyResetEntity.id] = reset.id
//                it[MonthlyResetEntity.lastReset] = reset.lastReset
//            }
//            reset
//        }
//
//
//    override suspend fun update(reset: MonthlyReset): MonthlyReset =
//        suspendTransaction(
//            db = db
//        ) {
//            MonthlyResetEntity.update ( { MonthlyResetEntity.id eq reset.id } ) {
//                it[MonthlyResetEntity.id] = reset.id
//                it[MonthlyResetEntity.lastReset] = reset.lastReset
//            }
//            reset
//        }
//}