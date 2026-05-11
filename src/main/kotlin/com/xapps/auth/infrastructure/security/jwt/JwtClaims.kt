package com.xapps.auth.infrastructure.security.jwt

object JwtClaims {

    const val SUBJECT = "sub"
    const val USER_ID = "userId"
    const val EMAIL = "email"
    const val ROLE = "role"
    const val ROLES = "roles"
    const val TOKEN_TYPE = "token_type"
    const val REFRESH = "refresh"
    const val ACCESS = "access"
    const val DEFAULT_ROLE = "USER"
}
