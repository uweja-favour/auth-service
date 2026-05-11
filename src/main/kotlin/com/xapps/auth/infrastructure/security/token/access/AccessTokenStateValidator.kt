package com.xapps.auth.infrastructure.security.token.access

import com.xapps.auth.persistence.repository.impl.AccessTokenRepository
import com.xapps.time.clock.ClockProvider
import org.springframework.stereotype.Component

@Component
class AccessTokenStateValidator(
    private val repository: AccessTokenRepository,
    private val clockProvider: ClockProvider
) {

    suspend fun isActive(jti: String): Boolean {
        val token = repository.findByJti(jti) ?: return false

        if (token.revoked) return false

        if (token.expiryAt <= clockProvider.now()) return false

        return true
    }
}