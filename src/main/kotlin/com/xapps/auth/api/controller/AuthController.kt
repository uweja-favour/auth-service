package com.xapps.auth.api.controller

import com.xapps.auth.core.web.ClientInfo
import com.xapps.auth.core.web.ClientMetadata
import com.xapps.auth.dto.ChangePasswordRequest
import com.xapps.auth.dto.EmptyResponse
import com.xapps.auth.dto.JwtAuthResponse
import com.xapps.auth.dto.LoginRequest
import com.xapps.auth.dto.RefreshTokenRequest
import com.xapps.auth.dto.SignupRequest
import com.xapps.auth.application.service.AuthApplicationService
import com.xapps.auth.dto.LogoutRequest
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/auth/student")
@RestController
class AuthController(
    private val authService: AuthApplicationService
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/signup")
    suspend fun signUp(
        @Valid @RequestBody request: SignupRequest,
        @ClientInfo metadata: ClientMetadata
    ): JwtAuthResponse {
        logger.info("Sign up attempt for email=${request.email}")
        val result = authService.registerUser(request, metadata)
        logger.info("Sign-up success for email=${request.email}")
        return result
    }

    @PostMapping("/login")
    suspend fun login(
        @Valid @RequestBody request: LoginRequest,
        @ClientInfo metadata: ClientMetadata
    ): JwtAuthResponse {
        logger.info("Login attempt for email=${request.email}")
        val result = authService.loginUser(request, metadata)
        logger.info("Login success for email=${request.email}")
        return result
    }

    @PostMapping("/logout")
    suspend fun logout(
        @Valid @RequestBody request: LogoutRequest,
        @ClientInfo metadata: ClientMetadata
    ): EmptyResponse {
        authService.logoutUser(request, metadata)
        return EmptyResponse()
    }

    @PostMapping("/change-password")
    suspend fun changePassword(
        authentication: Authentication,
        @Valid @RequestBody request: ChangePasswordRequest
    ): JwtAuthResponse {
        return authService.changePassword(request, authentication)
    }

    @PostMapping("/refresh")
    @Transactional
    suspend fun refreshTokens(
        @RequestBody request: RefreshTokenRequest,
        @ClientInfo metadata: ClientMetadata
    ): JwtAuthResponse {
        val result = authService.refreshTokens(request, metadata)
        logger.info("Token refresh SUCCESS")
        return result
    }
}