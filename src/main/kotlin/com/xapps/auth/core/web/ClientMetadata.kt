package com.xapps.auth.core.web

/**
 * Data class representing client metadata extracted from an HTTP request.
 */
data class ClientMetadata(
    val ipAddress: String,
    val userAgent: String,
    val acceptLanguage: String,
    val referer: String
)