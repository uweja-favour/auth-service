//package com.xapps.auth.admin.controller
//
//import com.xapps.auth.application.service.JwtService
//import com.xapps.auth.application.service.RefreshTokenService
//import com.xapps.auth.domain.entity.UserRole
//import com.xapps.auth.dto.JwtAuthResponse
//import com.xapps.auth.dto.LoginRequest
//import com.xapps.auth.dto.RefreshTokensRequest
//import com.xapps.auth.infrastructure.repository.user.UserRepository
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.validation.Valid
//import org.slf4j.LoggerFactory
//import org.springframework.http.ResponseEntity
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import java.time.Instant
//
//@RestController
//@RequestMapping("/api/auth/admin")
//class AdminAuthController(
//    private val userRepo: UserRepository,
//    private val passwordEncoder: PasswordEncoder,
//    private val jwtService: JwtService,
//    private val refreshTokenService: RefreshTokenService
//) {
//    private val logger = LoggerFactory.getLogger(AdminAuthController::class.java)
//
//    @PostMapping("/login")
//    fun login(
//        @Valid @RequestBody request: LoginRequest
//    ): ResponseEntity<*> {
//        val user = userRepo.findByUserId(request.email)
//        if (user == null || user.role != UserRole.ADMIN) {
//            return ResponseEntity.status(401).body(mapOf("error" to "Unauthorized admin access."))
//        }
//
//        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
//            return ResponseEntity.status(401).body(mapOf("error" to "Invalid credentials."))
//        }
//
//        val accessToken = jwtService.createNewAccessToken(user.userId, user.email, user.role.name)
//        val rawRefreshToken = refreshTokenService.createNewRefreshToken(user.userId)
//
//        logger.info("🔐 Admin login: ${user.email}")
//        return ResponseEntity.ok(JwtAuthResponse(accessToken, rawRefreshToken))
//    }
//
//
//    @PostMapping("/refresh")
//    fun refreshAdminToken(
//        @RequestBody request: RefreshTokensRequest,
//        requestContext: HttpServletRequest
//    ): ResponseEntity<*> {
//        return try {
//            val refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken)
//            val user = userRepo.findById(refreshToken.userId).orElse(null)
//                ?: return ResponseEntity.status(404).body(mapOf("error" to "Admin user not found"))
//
//            if (user.role != UserRole.ADMIN) {
//                logger.warn("⚠️ Refresh token used by non-admin: userId=${user.id}")
//                return ResponseEntity.status(403).body(mapOf("error" to "Forbidden"))
//            }
//
//            val newAccessToken = jwtService.createNewAccessToken(user.id, user.email, user.role.name)
//            val rawRefreshToken = refreshTokenService.revokeAndRotateRefreshToken(refreshToken)
//
//            logger.info("♻️ Admin token refreshed | userId=${user.id}, ip=${requestContext.remoteAddr}, time=${Instant.now()}")
//
//            ResponseEntity.ok(
//                JwtAuthResponse(
//                    accessToken = newAccessToken,
//                    refreshToken = rawRefreshToken
//                )
//            )
//        } catch (ex: IllegalArgumentException) {
//            logger.warn("❌ Invalid admin refresh token: ${ex.message}")
//            ResponseEntity.status(401).body(mapOf("error" to ex.message))
//        } catch (ex: IllegalStateException) {
//            logger.warn("⚠️ Expired/revoked admin refresh token: ${ex.message}")
//            ResponseEntity.status(403).body(mapOf("error" to ex.message))
//        }
//    }
//}
