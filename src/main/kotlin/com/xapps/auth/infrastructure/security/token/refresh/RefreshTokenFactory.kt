package com.xapps.auth.infrastructure.security.token.refresh

import com.xapps.auth.dto.RawRefreshToken
import com.xapps.auth.infrastructure.security.jwt.JwtClaims
import com.xapps.auth.infrastructure.security.jwt.JwtEncoders
import com.xapps.time.types.JavaInstant
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component

@Component
class RefreshTokenFactory(
    private val jwtEncoders: JwtEncoders,
) {

    // The refresh token must include minimal claims.
    // role and email must not be included.

    fun create(
        userId: String,
        jti: String,
        expiryMillis: Long
    ): RawRefreshToken {

        val now = JavaInstant.now()
        val expiry = now.plusMillis(expiryMillis)

        val claims = JwtClaimsSet.builder()
            .id(jti)
            .subject(userId)
            .issuedAt(now)
            .expiresAt(expiry)
            .claim(JwtClaims.TOKEN_TYPE, JwtClaims.REFRESH)
            .build()

        val headers = JwsHeader.with(MacAlgorithm.HS256).build()

        val token = jwtEncoders.accessJwtEncoder.encode(
            JwtEncoderParameters.from(headers, claims)
        )

        return RawRefreshToken.fromString(token.tokenValue)
    }
}