package com.xapps.auth.core.web

import org.springframework.web.server.ServerWebExchange

/**
 * Utility object that extracts client metadata from a ServerWebExchange.
 */
object ClientMetadataExtractor {

    fun extract(exchange: ServerWebExchange): ClientMetadata {
        val headers = exchange.request.headers

        // Determine client IP, considering common reverse proxy headers
        val ip = headers.getFirst("X-Forwarded-For")
            ?.split(",")
            ?.firstOrNull()
            ?.trim()
            ?: headers.getFirst("X-Real-IP")
            ?: exchange.request.remoteAddress?.address?.hostAddress
            ?: "unknown"

        val userAgent = headers.getFirst("User-Agent") ?: "unknown"
        val acceptLanguage = headers.getFirst("Accept-Language") ?: "unknown"
        val referer = headers.getFirst("Referer") ?: "unknown"

        return ClientMetadata(
            ipAddress = ip,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
            referer = referer
        )
    }
}