//package com.xapps.auth.infrastructure.repository.user.old
//
//import com.xapps.auth.persistence.entity.user.UserProfileEntity
//import com.xapps.auth.domain.model.user.UserProfile
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.singleOrNull
//import org.jetbrains.exposed.v1.core.ResultRow
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction
//import org.jetbrains.exposed.v1.r2dbc.deleteWhere
//import org.jetbrains.exposed.v1.r2dbc.insert
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Repository
//
//interface UserProfileRepository {
//    suspend fun R2dbcTransaction.create(userProfile: UserProfile): UserProfile
//
//    suspend fun R2dbcTransaction.update(userProfile: UserProfile): UserProfile
//
//    suspend fun R2dbcTransaction.findByUserId(userId: String): UserProfile?
//
//    suspend fun R2dbcTransaction.deleteByUserId(userId: String): Int
//}
//
//@Repository
//class UserProfileRepositoryImpl(
//    private val fcmDeviceRepository: FcmDeviceRepository
//) : UserProfileRepository {
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    val logger = LoggerFactory.getLogger(javaClass)
//
//    override suspend fun R2dbcTransaction.create(userProfile: UserProfile): UserProfile =
//        run {
//            UserProfileEntity.insert {
//                it[UserProfileEntity.userId] = userProfile.userId
//                it[UserProfileEntity.avatarS3Key] = userProfile.avatarS3Key
//            }
//            userProfile
//        }
//
//    override suspend fun R2dbcTransaction.update(userProfile: UserProfile): UserProfile =
//        run {
//            UserProfileEntity.update {
//                it[UserProfileEntity.userId] = userProfile.userId
//                it[UserProfileEntity.avatarS3Key] = userProfile.avatarS3Key
//            }
//            userProfile
//        }
//
//    override suspend fun R2dbcTransaction.findByUserId(userId: String): UserProfile? =
//       run {
//           UserProfileEntity.selectAll()
//               .where { UserProfileEntity.userId eq userId }
//               .singleOrNull()
//               ?.let { resultRow -> toUserProfile(resultRow) }
//       }
//
//    override suspend fun R2dbcTransaction.deleteByUserId(userId: String): Int =
//        run {
//            UserProfileEntity.deleteWhere { UserProfileEntity.userId eq UserProfileEntity.userId }
//        }
//
//    private suspend fun R2dbcTransaction.toUserProfile(row: ResultRow): UserProfile {
//        val userId = row[UserProfileEntity.userId]
//        val avatarS3Key = row[UserProfileEntity.avatarS3Key]
//        val fcmDevices = fcmDeviceRepository.run { findByUserId(userId) }
//
//        return UserProfile(
//            userId = userId,
//            avatarS3Key = avatarS3Key,
//            fcmDevices = fcmDevices
//        )
//    }
//}