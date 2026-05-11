package com.xapps.auth.infrastructure.security.jwt

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class JwtClaimsExtractor {

    fun extractUserId(jwt: Jwt): String =
        jwt.subject ?: error("Missing subject")

    fun extractEmail(jwt: Jwt): String =
        jwt.claims[JwtClaims.EMAIL] as? String ?: error("Missing email")

    fun extractRole(jwt: Jwt): String =
        jwt.claims[JwtClaims.ROLE] as? String ?: error("Missing role")

    fun extractJti(jwt: Jwt): String =
        jwt.id ?: error("Missing jti")
}