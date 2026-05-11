package com.xapps.auth.infrastructure.security

import com.xapps.auth.infrastructure.security.jwt.JwtClaimsExtractor
import com.xapps.auth.infrastructure.security.jwt.JwtParser
import com.xapps.auth.infrastructure.security.model.DomainUserPrincipal
import com.xapps.auth.infrastructure.security.token.access.AccessTokenStateValidator
import com.xapps.auth.persistence.repository.user.impl.UserRepository
import org.springframework.stereotype.Service

@Service
class AccessTokenAuthenticationService(
    private val parser: JwtParser,
    private val extractor: JwtClaimsExtractor,
    private val stateValidator: AccessTokenStateValidator,
    private val userRepository: UserRepository
) {

    suspend fun authenticate(accessTokenString: String): DomainUserPrincipal? {

        val jwt = runCatching {
            parser.parseAccessToken(accessTokenString)
        }.getOrElse { return null }

        val jti = extractor.extractJti(jwt) // jti is the 'jwt.id'

        if (!stateValidator.isActive(jti)) return null

        val userId = extractor.extractUserId(jwt)

        val user = userRepository.findByUserId(userId) ?: return null

        if (user.isBanned) return null

        return DomainUserPrincipal(
            user = user,
            email = user.email,
            userRole = user.role
        )
    }
}