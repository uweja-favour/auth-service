package com.xapps.auth.infrastructure.security.token.access

import com.xapps.auth.domain.model.user.User
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
        user: User,
        expiryMillis: Long,
        jti: String
    ): RawAccessToken {

        val now = Instant.now()
        val expiry = now.plusMillis(expiryMillis)

        val claims = JwtClaimsSet.builder()
            .id(jti)
            .subject(user.userId)
            .issuedAt(now)
            .expiresAt(expiry)
            .claim(JwtClaims.USER_ID, user.userId)
            .claim(JwtClaims.EMAIL, user.email)
            .claim(JwtClaims.USERNAME, user.username)
            .claim(JwtClaims.ROLE,  user.role.name)
            .claim(JwtClaims.TOKEN_TYPE, JwtClaims.ACCESS)
            .build()

        val headers = JwsHeader.with(MacAlgorithm.HS256).build()

        val token = jwtEncoders.accessJwtEncoder.encode(
            JwtEncoderParameters.from(headers, claims)
        )

        return RawAccessToken.fromString(token.tokenValue)
    }
}