//package com.xapps.auth.infrastructure.config
//
//import com.xapps.auth.application.service.UserDetailsServiceImpl
//import com.xapps.auth.infrastructure.security.token.access.AccessTokenService
//import com.xapps.auth.domain.model.UserPrincipal
//import com.xapps.auth.dto.RawAccessToken
//import com.xapps.auth.infrastructure.repository.user.UserRepository
//import com.xapps.platform.core.outcome.onFailure
//import com.xapps.platform.core.outcome.outcomeOf
//import kotlinx.coroutines.reactor.awaitSingle
//import kotlinx.coroutines.reactor.mono
//import org.slf4j.LoggerFactory
//import org.springframework.http.HttpMethod
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.context.ReactiveSecurityContextHolder
//import org.springframework.stereotype.Component
//import org.springframework.web.server.ServerWebExchange
//import org.springframework.web.server.WebFilter
//import org.springframework.web.server.WebFilterChain
//import reactor.core.publisher.Mono
//
///**
// * A WebFilter that intercepts all incoming HTTP requests and validates JWT tokens.
// * If a valid JWT is present, it sets the authentication in Spring Security's context.
// * Skips validation for public endpoints and OPTIONS requests.
// */
//@Component
//class JwtAuthWebFilter(
//    private val jwtService: AccessTokenService,                     // Service to handle JWT operations
//    private val userRepository: UserRepository,             // Repository to fetch user info
//    private val userDetailsService: UserDetailsServiceImpl  // Service to load Spring Security user details
//) : WebFilter {
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//
//    private companion object {
//        // List of endpoints that do not require JWT authentication
//        private val publicEndpoints = setOf(
//            "/api/auth/student/login",
//            "/api/auth/student/signup",
//            "/api/auth/student/refresh",
//            "/api/auth/validate-token"
//        )
//    }
//
//    /**
//     * Main filter method called for every HTTP request.
//     * @param exchange The HTTP request-response exchange
//     * @param chain The filter chain to continue processing
//     * @return A Mono<Void> indicating completion
//     */
//    override fun filter(
//        exchange: ServerWebExchange,
//        chain: WebFilterChain
//    ): Mono<Void> {
//        val request = exchange.request
//        val path = request.path.toString()
//        logger.info("FILTER JUST CAME IN: $path")
//
//        // Skip JWT validation for public endpoints or OPTIONS requests
//        if (publicEndpoints.contains(path) || request.method == HttpMethod.OPTIONS) {
//            logger.info("⏭️ Skipping JWT validation for $path")
//            return chain.filter(exchange)
//        }
//
//        // Extract the Authorization header
//        val authHeader = request.headers.getFirst("Authorization")
//        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
//            logger.info("❌ Missing or invalid Authorization header for $path")
//            return chain.filter(exchange)
//        }
//
//        // Extract the JWT token (remove "Bearer " prefix)
//        val token = authHeader.substring(7)
//        val rawAccessToken = RawAccessToken.fromString(token)
//
//        // Use a coroutine inside a Reactor Mono to validate JWT asynchronously
//        return Mono.deferContextual {
//            mono {
//                outcomeOf {
//                    // Check if the JWT token is valid
//                    if (!jwtService.isValidAccessToken(rawAccessToken)) {
//                        logger.debug("❌ Invalid JWT for $path")
//                        return@outcomeOf
//                    }
//
//                    // Extract the user ID from the token and fetch the user
//                    val userId = jwtService.extractUserId(rawAccessToken)
//                    val user = userRepository.findByUserId(userId)
//                    if (user == null) {
//                        logger.warn("❌ User not found for token userId=$userId")
//                        return@outcomeOf
//                    }
//
//                    // Check if the user is banned
//                    if (user.isBanned) {
//                        logger.warn("🚫 Banned user attempted access: ${user.email}")
//                        return@outcomeOf
//                    }
//
//                    // Load user details for Spring Security
//                    val email = jwtService.getEmailFromToken(rawAccessToken)
//                    val userDetails = userDetailsService.findByUsername(email).awaitSingle() as UserPrincipal
//
//                    // Create an authentication object and set it in the SecurityContext
//                    val auth = UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.authorities
//                    )
//                    ReactiveSecurityContextHolder.withAuthentication(auth)
//                    logger.debug("✅ JWT authentication set for user=${user.email}")
//
//                }.onFailure { e -> logger.warn("💥 JWT validation error for $path: ${e.message}", e) }
//            }.then(chain.filter(exchange)) // Continue the filter chain after validation
//        }
//    }
//}
