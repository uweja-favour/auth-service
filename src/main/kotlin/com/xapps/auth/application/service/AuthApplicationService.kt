package com.xapps.auth.application.service

import com.xapps.auth.core.service.BaseService
import com.xapps.auth.core.web.ClientMetadata
import com.xapps.auth.domain.exceptions.*
import com.xapps.auth.domain.model.user.User
import com.xapps.auth.domain.model.user.ensureNotBanned
import com.xapps.auth.dto.JwtAuthResponse
import com.xapps.auth.dto.ChangePasswordRequest
import com.xapps.auth.dto.LoginRequest
import com.xapps.auth.dto.LogoutRequest
import com.xapps.auth.dto.RefreshTokenRequest
import com.xapps.auth.persistence.repository.exceptions.UserDoesNotExistException
import com.xapps.auth.dto.SignupRequest
import com.xapps.auth.infrastructure.security.model.UserRole
import com.xapps.auth.infrastructure.security.token.access.AccessTokenService
import com.xapps.auth.infrastructure.security.token.refresh.RefreshTokenService
import com.xapps.auth.persistence.repository.user.impl.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthApplicationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenService: RefreshTokenService,
    private val jwtService: AccessTokenService
) : BaseService() {

    override val log = LoggerFactory.getLogger(javaClass)

    suspend fun registerUser(
        request: SignupRequest,
        metadata: ClientMetadata
    ): JwtAuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistException(request.email)
        }

        val user = User.createNew(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            username = request.username,
            role = UserRole.USER
        )

        userRepository.createNewUser(user)

        val refreshToken = refreshTokenService.issueToken(user.userId)

        val accessToken = jwtService.issueToken(
            userId = user.userId,
            email = user.email,
            role = user.role.name
        )

        return JwtAuthResponse(accessToken, refreshToken)
    }


    suspend fun loginUser(request: LoginRequest, metadata: ClientMetadata): JwtAuthResponse {
        log.info("LOGIN_1")
        val user = userRepository.findByEmail(request.email)
            ?: throw EmailNotFoundException(request.email)

        log.info("LOGIN_2")
        user.ensureNotBanned()

        log.info("LOGIN_3")
        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw IncorrectPasswordException()
        }

        log.info("LOGIN_4")
        val accessToken = jwtService.issueToken(
            userId = user.userId,
            email = user.email,
            role = user.role.name
        )

        log.info("LOGIN_5")
        val rawRefreshToken = refreshTokenService.issueToken(user.userId)
        return JwtAuthResponse(accessToken, rawRefreshToken)
    }


    suspend fun logoutUser(request: LogoutRequest, metadata: ClientMetadata) {
        val userId = refreshTokenService.findUserId(request.refreshToken)

        refreshTokenService.revokeAllTokensByUserId(userId)
        jwtService.revokeAllTokensByUserId(userId)
    }


    suspend fun changePassword(
        request: ChangePasswordRequest,
        authentication: Authentication
    ): JwtAuthResponse = withValidUser(authentication) { userPrincipal ->

        val user = userPrincipal.user

        if (!passwordEncoder.matches(request.oldPassword, user.passwordHash)) {
            throw IncorrectPasswordException()
        }

        userRepository.updateUser(
            user.copy(passwordHash = passwordEncoder.encode(request.newPassword))
        )

        issueNewTokens(user)
    }


    suspend fun refreshTokens(
        request: RefreshTokenRequest,
        metadata: ClientMetadata
    ): JwtAuthResponse {

        val token = refreshTokenService.verify(request.refreshToken)

        val user = userRepository.findByUserId(token.userId)
            ?: throw UserDoesNotExistException()

        user.ensureNotBanned()

        return issueNewTokens(user)
    }

    suspend fun issueNewTokens(user: User): JwtAuthResponse {
        return JwtAuthResponse(
            accessToken = jwtService.issueToken(
                userId = user.userId,
                user.email,
                user.role.name
            ),
            refreshToken = refreshTokenService.issueToken(user.userId)
        )
    }
}