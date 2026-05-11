package com.xapps.auth.infrastructure.security.token.access

import com.xapps.auth.dto.RawAccessToken
import com.xapps.auth.infrastructure.security.jwt.JwtClaims
import com.xapps.auth.infrastructure.security.jwt.JwtEncoders
import com.xapps.auth.infrastructure.security.jwt.JwtKeys
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class AccessTokenFactory(
    private val jwtEncoders: JwtEncoders,
) {

    fun create(
        userId: String,
        email: String,
        role: String,
        expiryMillis: Long,
        jti: String
    ): RawAccessToken {

        val now = Instant.now()
        val expiry = now.plusMillis(expiryMillis)

        val claims = JwtClaimsSet.builder()
            .id(jti)
            .subject(userId)
            .issuedAt(now)
            .expiresAt(expiry)
            .claim(JwtClaims.EMAIL, email)
            .claim(JwtClaims.ROLE, role)
            .claim(JwtClaims.TOKEN_TYPE, JwtClaims.ACCESS)
            .build()

        val headers = JwsHeader.with(MacAlgorithm.HS256).build()

        val token = jwtEncoders.accessJwtEncoder.encode(
            JwtEncoderParameters.from(headers, claims)
        )

        return RawAccessToken.fromString(token.tokenValue)
    }
}