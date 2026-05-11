package com.xapps.auth.infrastructure.security.jwt

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.stereotype.Component

@Component
class JwtParser(
    private val accessReactiveJwtDecoder: ReactiveJwtDecoder,
) {

    // For parsing only access token string
    fun parseAccessToken(accessTokenString: String): Jwt {
        return accessReactiveJwtDecoder.decode(accessTokenString).block()
            ?: throw IllegalArgumentException("Invalid JWT")
    }
}