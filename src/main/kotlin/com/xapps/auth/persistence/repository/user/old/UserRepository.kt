//package com.xapps.auth.infrastructure.repository.user
//
//import com.xapps.auth.persistence.entity.user.UserEntity
//import com.xapps.auth.domain.model.user.User
//import com.xapps.auth.domain.exceptions.UserAlreadyExistException
//import com.xapps.auth.persistence.repository.UserDoesNotExistException
//import com.xapps.auth.infrastructure.repository.user.old.UserProfileRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.singleOrNull
//import org.jetbrains.exposed.v1.core.ResultRow
//import org.jetbrains.exposed.v1.core.*
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction
//import org.jetbrains.exposed.v1.r2dbc.insert
//import org.jetbrains.exposed.v1.r2dbc.selectAll
//import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
//import org.jetbrains.exposed.v1.r2dbc.update
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Repository
//
//interface UserRepository {
//    suspend fun createNewUser(user: User): User
//    suspend fun updateUser(user: User): User
//    suspend fun findByUserId(userId: String): User?
//    suspend fun findByEmail(email: String): User?
//    suspend fun existsByEmail(email: String): Boolean
//}
//
//@Repository
//class UserRepositoryImpl(
//    private val userProfileRepository: UserProfileRepository,
//    private val userSubscriptionRepository: UserSubscriptionRepository,
//    private val db: R2dbcDatabase
//) : UserRepository {
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//
//    companion object { private val ioDispatcher = Dispatchers.IO }
//
//    override suspend fun createNewUser(user: User): User =
//        suspendTransaction(
//            db = db
//        ) {
//            logger.info("1")
//            val existingUser = UserEntity.selectAll()
//                .where { UserEntity.userId eq user.userId }
//                .singleOrNull()
//
//            logger.info("2")
//            if (existingUser != null) {
//                throw UserAlreadyExistException()
//            }
//
//            logger.info("3")
//            UserEntity.insert {
//                it[UserEntity.userId] = user.userId
//                it[UserEntity.email] = user.email
//                it[UserEntity.username] = user.username
//                it[UserEntity.passwordHash] = user.passwordHash
//                it[UserEntity.role] = user.role
//                it[UserEntity.createdAt] = user.createdAt
//            }
//
//            logger.info("4")
//
//            userProfileRepository.run { create(user.profile) }
//
//            logger.info("5")
//            user
//        }
//
//    override suspend fun updateUser(user: User): User {
//        suspendTransaction(
//            db = db
//        ) {
//            val existingUser = UserEntity.selectAll()
//                .where { UserEntity.userId eq user.userId }
//                .singleOrNull()
//
//            if (existingUser == null) {
//                throw UserDoesNotExistException()
//            }
//
//            UserEntity.update( { UserEntity.userId eq user.userId } ) {
//                it[UserEntity.userId] = user.userId
//                it[UserEntity.email] = user.email
//                it[UserEntity.username] = user.username
//                it[UserEntity.passwordHash] = user.passwordHash
//                it[UserEntity.role] = user.role
//                it[UserEntity.createdAt] = user.createdAt
//            }
//
//            userProfileRepository.run { update(user.profile) }
//        }
//
//        return user
//    }
//
//    override suspend fun findByUserId(userId: String): User? =
//        suspendTransaction(
//            db = db
//        ) {
//            val userRow = UserEntity
//                .selectAll()
//                .where { UserEntity.userId eq userId }
//                .singleOrNull()
//                ?: return@suspendTransaction null
//
//            return@suspendTransaction toUser(userRow)
//        }
//
//    override suspend fun findByEmail(email: String): User? =
//        suspendTransaction(
//            db = db
//        ) {
//            val userRow = UserEntity
//                .selectAll()
//                .where { UserEntity.email eq email }
//                .singleOrNull()
//                ?: return@suspendTransaction null
//
//
//            println("Columns in ResultRow:")
//            userRow.fieldIndex.keys.forEach { column ->
//                println("$column = ${userRow[column]}")
//            }
//            return@suspendTransaction toUser(userRow)
//        }
//
//    override suspend fun existsByEmail(email: String): Boolean =
//        suspendTransaction(
//            db = db
//        ) {
//            UserEntity.selectAll()
//                .where { UserEntity.email eq email }
//                .singleOrNull()
//                ?: return@suspendTransaction false
//
//            return@suspendTransaction true
//        }
//
//    private suspend fun R2dbcTransaction.toUser(row: ResultRow): User? {
//        println("About to get user id")
//
//        val userId = row[UserEntity.userId]
//        println("About to get user profile")
//        val userProfile = userProfileRepository.run { findByUserId(userId) }
//            ?: return null
//
//        println("About to get user subscription")
//        val userSubscription = userSubscriptionRepository.run { findByUserId(userId) }
//
//        println("About to return")
//
//        return User(
//            userId = userId,
//            email = row[UserEntity.email],
//            username = row[UserEntity.username],
//            passwordHash = row[UserEntity.passwordHash],
//            role = row[UserEntity.role],
//            isBanned = row[UserEntity.isBanned],
//            createdAt = row[UserEntity.createdAt],
//            profile = userProfile,
//            subscription = userSubscription
//        )
//    }
//}