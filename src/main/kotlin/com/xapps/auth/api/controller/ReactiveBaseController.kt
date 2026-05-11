package com.xapps.auth.api.controller

import com.xapps.auth.infrastructure.security.model.DomainUserPrincipal
import com.xapps.platform.core.outcome.outcomeOf
import com.xapps.platform.core.outcome.respond
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.ReactiveSecurityContextHolder

abstract class ReactiveBaseController {

    val blogger = LoggerFactory.getLogger(this::class.java)
    /**
     * Retrieves the currently authenticated user principal from the reactive security context.
     *
     * This function suspends until the current reactive security context is available,
     * extracts the authentication, and returns the principal as [DomainUserPrincipal].
     *
     * @return the authenticated [DomainUserPrincipal]
     * @throws IllegalStateException if no user is authenticated or the principal cannot be cast
     */
    suspend fun getAuthenticatedUserPrincipal(): DomainUserPrincipal {
        return ReactiveSecurityContextHolder.getContext()
            .awaitSingleOrNull()
            ?.authentication
            ?.principal as? DomainUserPrincipal
            ?: throw IllegalStateException("User is not authenticated")
    }

    suspend inline fun <T> handle(
        operation: String,
        crossinline block: suspend () -> T
    ): T =
        outcomeOf {
            blogger.info("REQUEST COMING IN: $operation")
            block()
        }.respond(
            onSuccess = { it },
            onFailure = {
                blogger.error("$operation failed", it.exception)
                throw it.exception
            }
        )
}