package com.xapps.auth.infrastructure.security.jwt

import org.springframework.security.oauth2.jwt.JwtEncoder

data class JwtEncoders(
    val accessJwtEncoder: JwtEncoder,
    val refreshJwtEncoder: JwtEncoder
)