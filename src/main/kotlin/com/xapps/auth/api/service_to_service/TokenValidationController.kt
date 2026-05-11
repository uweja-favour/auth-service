//package com.xapps.auth.api.service_to_service
//
//import com.xapps.auth.infrastructure.security.token.access.AccessTokenService
//import com.xapps.auth.api.controller.BaseController
//import com.xapps.auth.dto.service_to_service.ValidationRequest
//import com.xapps.auth.dto.service_to_service.ValidationResponse
//import kotlinx.coroutines.supervisorScope
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/api/auth")
//class TokenValidationController(
//    private val jwtService: AccessTokenService // service that validates JWT
//) : BaseController() {
//
//    override val logger: Logger = LoggerFactory.getLogger(javaClass)
//
//    @PostMapping("/validate-token")
//    suspend fun validateToken(@RequestBody request: ValidationRequest): ResponseEntity<ValidationResponse> {
//        return supervisorScope {
//            logger.info("TokenValidationController called!")
//
//            jwtService.validateAndParse(request.rawAccessToken)
//            jwtService.validateAndParse(request.rawAccessToken)
//                .takeIf { it != null }
//                ?.let { claims ->
//                    logger.info("Claims: $claims")
//
//                    with(claims) { ResponseEntity.ok(ValidationResponse.valid(this@with)) }
//                } ?: ResponseEntity.ok(ValidationResponse.invalid())
//        }
//    }
//}