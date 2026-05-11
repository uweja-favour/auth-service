//package com.xapps.auth.infrastructure.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpMethod
//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder
//import org.springframework.security.config.web.server.ServerHttpSecurity
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.server.SecurityWebFilterChain
//import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.reactive.CorsConfigurationSource
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
//
///**
// * Security configuration for the application using Spring WebFlux.
// * - Enables reactive method security
// * - Configures JWT authentication via custom filter and entry point
// * - Sets up CORS, CSRF, and endpoint access rules
// */
//@Configuration
//@EnableReactiveMethodSecurity
//class SecurityConfig(
//    private val jwtAuthWebFilter: JwtAuthWebFilter,  // Custom JWT filter
//    private val jwtAuthEntryPoint: JwtAuthEntryPoint // Custom authentication failure handler
//) {
//
//    /**
//     * Bean for encoding passwords using BCrypt.
//     * Used by Spring Security when storing/verifying passwords.
//     */
//    @Bean
//    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
//
//    /**
//     * Configures the security filter chain for reactive WebFlux endpoints.
//     */
//    @Bean
//    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
//        return http
//            // 1️⃣ CORS configuration
//            .cors { it.configurationSource(corsConfigurationSource()) }
//
//            // 2️⃣ Disable CSRF (not needed for APIs)
//            .csrf { it.disable() }
//
//            // 3️⃣ Handle authentication failures using our JwtAuthEntryPoint
//            .exceptionHandling { exceptions ->
//                exceptions.authenticationEntryPoint { exchange, exception ->
//                    jwtAuthEntryPoint.commence(exchange, exception)
//                }
//            }
//
//            // 4️⃣ Disable default session-based security (stateless API)
//            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//
//            // 5️⃣ Define endpoint access rules
//            .authorizeExchange { exchanges ->
//                exchanges
//                    .pathMatchers("/api/auth/validate-token").permitAll()
//                    .pathMatchers("/api/auth/**").permitAll()           // Public auth endpoints
//                    .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
//                    .pathMatchers("/api/quiz/**").authenticated()       // Require auth for quiz endpoints
//                    .pathMatchers("/api/account/update-fcm").authenticated() // Require auth
//                    .anyExchange().authenticated()                       // All other endpoints require auth
//            }
//
//            // 6️⃣ Add our JWT filter before Spring's authentication filter
//            .addFilterBefore(jwtAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//
//            // 7️⃣ Build and return the security chain
//            .build()
//    }
//
//    /**
//     * CORS configuration bean for handling cross-origin requests.
//     * Important for allowing the Android emulator to call the API.
//     */
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val config = CorsConfiguration()
//        config.allowedOrigins = listOf("http://10.0.2.2:5177, http://localhost:8082, http://localhost:8083") // Android emulator origin and self test quiz service
//        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        config.allowedHeaders = listOf("*")
//        config.allowCredentials = true // required for sending Authorization headers or cookies
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", config)
//        return source
//    }
//
//    // ✅ AuthenticationManager bean is not needed in WebFlux when using reactive security
////    @Bean
////    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
////        return config.authenticationManager
////    }
//}
