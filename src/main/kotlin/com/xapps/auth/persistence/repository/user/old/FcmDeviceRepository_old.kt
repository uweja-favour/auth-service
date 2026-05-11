//package com.xapps.auth.infrastructure.repository.user
//
//import com.xapps.auth.persistence.entity.user.FcmDeviceEntity
//import com.xapps.auth.domain.model.user.FcmDevice
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.singleOrNull
//import kotlinx.coroutines.flow.toList
//import org.jetbrains.exposed.v1.core.ResultRow
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction
//import org.jetbrains.exposed.v1.r2dbc.deleteWhere
//import org.jetbrains.exposed.v1.r2dbc.select
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.jetbrains.exposed.v1.r2dbc.upsert
//import org.springframework.stereotype.Repository
//
//interface FcmDeviceRepository {
//
//    suspend fun R2dbcTransaction.registerDevice(device: FcmDevice): FcmDevice
//
//    suspend fun R2dbcTransaction.findByUserId(userId: String): List<FcmDevice>
//
//    suspend fun R2dbcTransaction.deleteByToken(token: String): Int
//}
//
//@Repository
//class FcmDeviceRepositoryImpl : FcmDeviceRepository {
//
//    companion object {
//        private val ioDispatcher = Dispatchers.IO
//    }
//
//    override suspend fun R2dbcTransaction.registerDevice(device: FcmDevice) =
//        run {
//            val existing = FcmDeviceEntity.select ( FcmDeviceEntity.token eq device.token )
//                .singleOrNull()
//
//            if (existing != null) {
//                FcmDeviceEntity.update(where = { FcmDeviceEntity.id eq existing[FcmDeviceEntity.id] }) {
//                    it[FcmDeviceEntity.userId] = device.userId
//                    it[FcmDeviceEntity.platform] = device.platform
//                    it[FcmDeviceEntity.deviceModel] = device.deviceModel
//                    it[FcmDeviceEntity.lastActiveAt] = device.lastActiveAt
//                    it[FcmDeviceEntity.updatedAt] = device.updatedAt
//                }
//            } else {
//                FcmDeviceEntity.upsert {
//                    it[FcmDeviceEntity.id] = device.fcmDeviceId
//                    it[FcmDeviceEntity.userId] = device.userId
//                    it[FcmDeviceEntity.token] = device.token
//                    it[FcmDeviceEntity.platform] = device.platform
//                    it[FcmDeviceEntity.deviceModel] = device.deviceModel
//                    it[FcmDeviceEntity.lastActiveAt] = device.lastActiveAt
//                    it[FcmDeviceEntity.createdAt] = device.createdAt
//                    it[FcmDeviceEntity.updatedAt] = device.updatedAt
//                }
//            }
//            device
//        }
//
//
//    override suspend fun R2dbcTransaction.findByUserId(userId: String): List<FcmDevice> =
//        run {
//            FcmDeviceEntity.select ( FcmDeviceEntity.userId eq userId )
//                .map { it.toFcmDevice() }
//                .toList()
//        }
//
//    override suspend fun R2dbcTransaction.deleteByToken(token: String) =
//        run {
//            FcmDeviceEntity.deleteWhere { FcmDeviceEntity.token eq token }
//        }
//
//    private fun ResultRow.toFcmDevice() = FcmDevice(
//        fcmDeviceId = this[FcmDeviceEntity.id],
//        userId = this[FcmDeviceEntity.userId],
//        token = this[FcmDeviceEntity.token],
//        platform = this[FcmDeviceEntity.platform],
//        deviceModel = this[FcmDeviceEntity.deviceModel],
//        lastActiveAt = this[FcmDeviceEntity.lastActiveAt],
//        createdAt = this[FcmDeviceEntity.createdAt],
//        updatedAt = this[FcmDeviceEntity.updatedAt]
//    )
//}
