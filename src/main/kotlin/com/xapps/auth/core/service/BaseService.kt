package com.xapps.auth.core.service

import com.xapps.auth.core.web.ClientMetadata
import com.xapps.auth.core.service.exceptions.InvalidAuthentication
import com.xapps.auth.domain.model.user.ensureNotBanned
import com.xapps.auth.infrastructure.security.model.DomainUserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
open class BaseService {

    val log: Logger = LoggerFactory.getLogger("BaseService")

    protected fun extractClientIp(exchange: ServerWebExchange): String {
        val headers = exchange.request.headers

        return headers.getFirst("X-Forwarded-For")
            ?.split(",")
            ?.firstOrNull()
            ?.trim()
            ?: headers.getFirst("X-Real-IP")
            ?: exchange.request.remoteAddress?.address?.hostAddress
            ?: "unknown"
    }

    protected fun extractUserAgent(exchange: ServerWebExchange): String {
        val headers = exchange.request.headers
        return headers.getFirst("User-Agent") ?: "unknown"
    }

    protected fun collectClientMetadata(exchange: ServerWebExchange): ClientMetadata {
        val ip = extractClientIp(exchange)
        val userAgent = extractUserAgent(exchange)

        val headers = exchange.request.headers
        val acceptLang = headers.getFirst("Accept-Language") ?: "unknown"
        val referer = headers.getFirst("Referer") ?: "unknown"

        return ClientMetadata(
            ipAddress = ip,
            userAgent = userAgent,
            acceptLanguage = acceptLang,
            referer = referer
        )
    }

    protected suspend fun <T> withValidUser(
        authentication: Authentication,
        action: suspend (DomainUserPrincipal) -> T
    ): T {
        val userPrincipal = authentication.principal as? DomainUserPrincipal
            ?: throw InvalidAuthentication()

        val user = userPrincipal.user

        user.ensureNotBanned()

        return action(userPrincipal)
    }
}