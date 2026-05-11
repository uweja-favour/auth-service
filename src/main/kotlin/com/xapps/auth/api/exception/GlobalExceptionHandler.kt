package com.xapps.auth.api.exception

import com.xapps.auth.domain.exceptions.EmailAlreadyExistException
import com.xapps.auth.domain.exceptions.EmailNotFoundException
import com.xapps.auth.domain.exceptions.IncorrectPasswordException
import com.xapps.auth.domain.exceptions.RefreshTokenExpiredException
import com.xapps.auth.domain.exceptions.RefreshTokenInvalidException
import com.xapps.auth.domain.exceptions.RefreshTokenRevokedException
import com.xapps.auth.domain.exceptions.UserBannedException
import com.xapps.dto.ApiErrorResponse
import com.xapps.dto.ApiErrorResponseType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(EmailAlreadyExistException::class)
    fun handleEmailExists(ex: EmailAlreadyExistException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleEmailExists")

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.EMAIL_ALREADY_EXISTS,
                message = "This email is already registered"
            )
        )
    }

    @ExceptionHandler(EmailNotFoundException::class)
    fun handleEmailNotFound(ex: EmailNotFoundException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleEmailNotFound")

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.INVALID_CREDENTIALS,
                message = "No account found with this email"
            )
        )
    }

    @ExceptionHandler(IncorrectPasswordException::class)
    fun handleWrongPassword(ex: IncorrectPasswordException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleWrongPassword")

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.INVALID_CREDENTIALS,
                message = "Incorrect email or password"
            )
        )
    }

    @ExceptionHandler(UserBannedException::class)
    fun handleBanned(ex: UserBannedException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleBanned")

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.USER_BANNED,
                message = "Your account has been banned"
            )
        )
    }

    @ExceptionHandler(RefreshTokenExpiredException::class)
    fun handleRefreshTokenExpired(ex: RefreshTokenExpiredException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleRefreshTokenExpired")

        // returns a 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.REFRESH_TOKEN_EXPIRED,
                message = "Refresh token has expired. Please log in again."
            )
        )
    }

    @ExceptionHandler(RefreshTokenRevokedException::class)
    fun handleRefreshTokenRevoked(ex: RefreshTokenRevokedException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleRefreshTokenRevoked")

        // returns a 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.REFRESH_TOKEN_REVOKED,
                message = "Refresh token has been revoked. Please log in again."
            )
        )
    }

    @ExceptionHandler(RefreshTokenInvalidException::class)
    fun handleRefreshTokenInvalid(ex: RefreshTokenInvalidException): ResponseEntity<ApiErrorResponse> {
        logger.info("${javaClass.simpleName} ran handleRefreshTokenInvalid")

        // returns a 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                errorType = ApiErrorResponseType.REFRESH_TOKEN_INVALID,
                message = "Refresh token is invalid. Please log in again."
            )
        )
    }
}