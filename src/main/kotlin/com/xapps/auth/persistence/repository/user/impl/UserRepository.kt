package com.xapps.auth.persistence.repository.user.impl

import com.xapps.auth.domain.exceptions.UserAlreadyExistException
import com.xapps.auth.domain.model.user.User
import com.xapps.auth.domain.model.user.UserProfile
import com.xapps.auth.infrastructure.security.model.UserRole
import com.xapps.auth.persistence.entity.user.UserDocument
import com.xapps.auth.persistence.repository.exceptions.UserDoesNotExistException
import com.xapps.auth.persistence.repository.user.MongoUserRepository
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface UserRepository {

    suspend fun createNewUser(user: User): User

    suspend fun updateUser(user: User): User

    suspend fun findByUserId(userId: String): User?

    suspend fun findByEmail(email: String): User?

    suspend fun existsByEmail(email: String): Boolean
}

@Repository
class UserRepositoryImpl(
    private val mongoRepository: MongoUserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository
) : UserRepository {

    override suspend fun createNewUser(user: User): User {

        val existingUser = mongoRepository.findByUserId(user.userId)

        if (existingUser != null) {
            throw UserAlreadyExistException()
        }

        mongoRepository.saveUpserting(user.toDocument())

        userProfileRepository.create(user.profile)

        return user
    }

    override suspend fun updateUser(user: User): User {

        val existingUser = mongoRepository.findByUserId(user.userId)
            ?: throw UserDoesNotExistException()

        val updatedDocument = existingUser.copy(
            email = user.email,
            username = user.username,
            passwordHash = user.passwordHash,
            roleCode = user.role.code,
            isBanned = user.isBanned,
            createdAt = user.createdAt
        )

        mongoRepository.saveUpserting(updatedDocument)

        userProfileRepository.update(user.profile)

        return user
    }

    override suspend fun findByUserId(userId: String): User? {

        val document = mongoRepository.findByUserId(userId)
            ?: return null

        return document.toDomain()
    }

    override suspend fun findByEmail(email: String): User? {

        val document = mongoRepository.findByEmail(email)
            ?: return null

        return document.toDomain()
    }

    override suspend fun existsByEmail(email: String): Boolean {
        return mongoRepository.existsByEmail(email)
    }

    private suspend fun UserDocument.toDomain(): User {

        val userProfile = userProfileRepository.findByUserId(userId)
            ?: return User(
                userId = userId,
                email = email,
                username = username,
                passwordHash = passwordHash,
                role = UserRole.fromCode(roleCode),
                isBanned = isBanned,
                createdAt = createdAt,
                profile = UserProfile(
                    userId = userId,
                    avatarS3Key = "",
                    fcmDevices = emptyList()
                ),
                subscription = userSubscriptionRepository.findByUserId(userId)
            )

        val userSubscription = userSubscriptionRepository.findByUserId(userId)

        return User(
            userId = userId,
            email = email,
            username = username,
            passwordHash = passwordHash,
            role = UserRole.fromCode(roleCode),
            isBanned = isBanned,
            createdAt = createdAt,
            profile = userProfile,
            subscription = userSubscription
        )
    }

    private fun User.toDocument(): UserDocument {
        return UserDocument(
            userId = userId,
            email = email,
            username = username,
            passwordHash = passwordHash,
            roleCode = role.code,
            isBanned = isBanned,
            createdAt = createdAt
        )
    }
}