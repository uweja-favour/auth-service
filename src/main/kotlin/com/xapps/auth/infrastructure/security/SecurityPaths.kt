package com.xapps.auth.infrastructure.security

object SecurityPaths {
    val PUBLIC = arrayOf(
        "/api/auth/student/login",
        "/api/auth/student/signup",
        "/api/auth/student/refresh",
        "/api/auth/student/logout",
        "/api/auth/validate-token"
    )
}