package com.xapps.auth.persistence.repository.user.impl

import com.xapps.auth.domain.exceptions.UserAlreadyExistException
import com.xapps.auth.domain.model.user.User
import com.xapps.auth.domain.model.user.UserProfile
import com.xapps.auth.infrastructure.security.model.UserRole
import com.xapps.auth.persistence.entity.user.UserDocument
import com.xapps.auth.persistence.entity.user.UserProfileDocument
import com.xapps.auth.persistence.mapper.FileDataMapper
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

    suspend fun existsByUsername(userName: String): Boolean
}

@Repository
class UserRepositoryImpl(
    private val mongoRepository: MongoUserRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
    private val fileDataMapper: FileDataMapper
) : UserRepository {

    override suspend fun createNewUser(user: User): User {

        val existingUser = mongoRepository.findByUserId(user.userId)

        if (existingUser != null) {
            throw UserAlreadyExistException()
        }

        mongoRepository.saveUpserting(user.toDocument())

        return user
    }

    override suspend fun updateUser(user: User): User {

        mongoRepository.findByUserId(user.userId)
            ?: throw UserDoesNotExistException()

        mongoRepository.saveUpserting(user.toDocument())

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

    override suspend fun existsByUsername(userName: String): Boolean {
        return mongoRepository.existsByUsername(userName)
    }

    private suspend fun UserDocument.toDomain(): User {

        val userSubscription = userSubscriptionRepository.findByUserId(userId)

        return User(
            userId = userId,
            email = email,
            username = username,
            passwordHash = passwordHash,
            role = UserRole.fromCode(roleCode),
            isBanned = isBanned,
            createdAt = createdAt,
            profile = profile.toDomain(),
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
            createdAt = createdAt,
            profile = profile.toDocument()
        )
    }

    private fun UserProfileDocument.toDomain(): UserProfile {
        return UserProfile(
            image = image?.let { fileDataMapper.toDomain(it) },
            userId = userId
        )
    }

    private fun UserProfile.toDocument(): UserProfileDocument {
        return UserProfileDocument(
            image = image?.let { fileDataMapper.toDocument(it) },
            userId = userId
        )
    }
}
