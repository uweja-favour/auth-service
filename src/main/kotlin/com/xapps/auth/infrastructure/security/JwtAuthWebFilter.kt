package com.xapps.auth.infrastructure.security

import com.xapps.auth.infrastructure.security.model.AuthenticatedUserToken
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtAuthWebFilter(
    private val tokenExtractor: BearerTokenExtractor,
    private val authValidator: AccessTokenAuthenticationService,
    @Value("\${security.auth.enabled:true}")
    private val authEnabled: Boolean
) : WebFilter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        if (!authEnabled) {
            logger.warn("Auth disabled — bypassing security")
            return chain.filter(exchange)
        }

        val request = exchange.request

        if (request.method == HttpMethod.OPTIONS) {
            return chain.filter(exchange)
        }

        logger.info("Path: ${exchange.request.path}")


        val token = tokenExtractor.extractBearerToken(request)
            ?: return chain.filter(exchange)

        return mono {
            authValidator.authenticate(token)
        }.flatMap { principal ->

            if (principal == null) {
                return@flatMap chain.filter(exchange)
            }

            val auth = AuthenticatedUserToken(
                principal,
                principal.authorities
            )

            chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
        }
    }
}