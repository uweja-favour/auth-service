package com.xapps.auth.api.controller

import com.xapps.auth.domain.model.user.withIdentifyingInfo
import com.xapps.auth.infrastructure.security.model.DomainUserPrincipal
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Instant

abstract class BaseController {
    abstract val logger: Logger

    protected fun <T> unauthorized(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.UNAUTHORIZED, authentication)

    protected fun <T> forbidden(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.FORBIDDEN, authentication)

    protected fun <T> notFound(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.NOT_FOUND, authentication)

    protected fun <T> paymentRequired(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.PAYMENT_REQUIRED, authentication)

    protected fun <T> internalServerError(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.INTERNAL_SERVER_ERROR, authentication)

    protected fun <T> badRequest(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.BAD_REQUEST, authentication)

    protected fun <T> badGateway(authentication: Authentication? = null): ResponseEntity<T> =
        logAndReturn(HttpStatus.BAD_GATEWAY, authentication)

    /**
     * Returns a 409 Conflict response with an optional message payload.
     * Use this for DB unique constraint violations or other conflicts.
     */
    fun <T> conflict(message: String = "Conflict"): ResponseEntity<T> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }

    protected fun <T> ok(data: T, authentication: Authentication? = null): ResponseEntity<T> {
        if (data != null && logger.isDebugEnabled && authentication != null) {
            val userInfo = getUserInfo(authentication)
            logger.debug("✅ Successful response for user: $userInfo")
        }
        return ResponseEntity.ok(data)
    }

    protected fun <T> accepted(data: T, msg: String = "", authentication: Authentication? = null): ResponseEntity<T> {
        if (data != null && logger.isDebugEnabled && authentication != null) {
            val userInfo = getUserInfo(authentication)
            logger.debug("✅ Accepted response for user: $userInfo")
        }
        return ResponseEntity.accepted().body(data)
    }

    /**
     * Get user information from the current security context
     */
    private fun getCurrentUser(): DomainUserPrincipal? {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            authentication?.principal as? DomainUserPrincipal
        } catch (e: Exception) {
            logger.warn("Failed to get current user from security context: ${e.message}")
            null
        }
    }

    /**
     * Get user information from authentication parameter
     */
    private fun getUserFromAuth(authentication: Authentication?): DomainUserPrincipal? {
        return try {
            authentication?.principal as? DomainUserPrincipal
        } catch (e: Exception) {
            logger.warn("Failed to get user from authentication: ${e.message}")
            null
        }
    }

    /**
     * Get user identifying info for logging (safe string representation)
     */
    fun getUserInfo(authentication: Authentication? = null): String {
        val userPrincipal = getUserFromAuth(authentication) ?: getCurrentUser()
        return userPrincipal?.userId ?: "unknown_user_info"
    }

    /**
     * Execute a block with user validation, handling common error cases
     */
    protected inline fun <T> withValidUser(
        authentication: Authentication?,
        crossinline action: (DomainUserPrincipal) -> ResponseEntity<T>
    ): ResponseEntity<T> {
        return try {
            val userPrincipal = authentication?.principal as? DomainUserPrincipal
                ?: return unauthorized(authentication)
            action(userPrincipal)
        } catch (e: Exception) {
            logger.error("❌ Error executing request for user ${getUserInfo(authentication)}: ${e.message}", e)
            internalServerError(authentication)
        }
    }


    /**
     * Execute a block with user validation, handling common error cases
     */
    protected suspend inline fun <T> suspendWithValidUser(
        authentication: Authentication?,
        crossinline action: suspend (DomainUserPrincipal) -> ResponseEntity<T>
    ): ResponseEntity<T> {
        return try {
            val userPrincipal = authentication?.principal as? DomainUserPrincipal
                ?: return unauthorized(authentication)
            action(userPrincipal)
        } catch (e: Exception) {
            logger.error("❌ Error executing request for user ${getUserInfo(authentication)}: ${e.message}", e)
            internalServerError(authentication)
        }
    }

    private fun <T> logAndReturn(httpStatus: HttpStatus, authentication: Authentication? = null): ResponseEntity<T> {
        if (authentication != null) {
            val userInfo = getUserInfo(authentication)
            logger.error("❌ HttpStatus ERROR: $httpStatus occurred at ${Instant.now()} for user: $userInfo")
        }
        return ResponseEntity.status(httpStatus).build()
    }
}