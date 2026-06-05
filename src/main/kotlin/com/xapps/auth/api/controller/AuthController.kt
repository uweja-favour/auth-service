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
import com.xapps.auth.domain.model.user.getProfileDTO
import com.xapps.auth.dto.LogoutRequest
import com.xapps.auth.dto.ProfileDTO
import com.xapps.auth.dto.UpdateProfileRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/auth/student")
@RestController
class AuthController(
    private val authService: AuthApplicationService
) : ReactiveBaseController() {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/signup")
    suspend fun signUp(
        @Valid @RequestBody request: SignupRequest,
        @ClientInfo metadata: ClientMetadata
    ): JwtAuthResponse {
        log.info("Sign up attempt for email=${request.email}")
        val result = authService.registerUser(request, metadata)
        log.info("Sign-up success for email=${request.email}")
        return result
    }

    @PostMapping("/login")
    suspend fun login(
        @Valid @RequestBody request: LoginRequest,
        @ClientInfo metadata: ClientMetadata
    ): JwtAuthResponse {
        log.info("Login attempt for email=${request.email}")
        val result = authService.loginUser(request, metadata)
        log.info("Login success for email=${request.email}")
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

    @PostMapping("/update-profile")
    suspend fun updateProfile(
        @RequestBody request: UpdateProfileRequest
    ): ProfileDTO =
        handle("update-profile") {

            authService.updateProfile(request)
        }

    @GetMapping("/profile")
    suspend fun getProfile(): ProfileDTO  =
        handle("getProfile") {
            val user = getAuthenticatedUserPrincipal().user
            user.getProfileDTO()
        }

    @PostMapping("/refresh")
    @Transactional
    suspend fun refreshTokens(
        @RequestBody request: RefreshTokenRequest,
        @ClientInfo metadata: ClientMetadata
    ): JwtAuthResponse {
        val result = authService.refreshTokens(request, metadata)
        log.info("Token refresh SUCCESS")
        return result
    }
}