package com.xapps.auth.infrastructure.security.jwt

import javax.crypto.SecretKey

data class JwtKeys(
    val accessSecretKey: SecretKey,
    val refreshSecretKey: SecretKey
)