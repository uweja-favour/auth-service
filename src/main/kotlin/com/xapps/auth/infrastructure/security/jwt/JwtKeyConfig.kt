package com.xapps.auth.infrastructure.security.jwt

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtKeyConfig {

    @Bean
    fun jwtKeys(
        @Value("\${jwt.accessSecret}") accessSecret: String,
        @Value("\${jwt.refreshSecret}") refreshSecret: String
    ): JwtKeys {

        require(accessSecret.length >= 32 && refreshSecret.length >= 32) {
            "JWT secrets must be at least 32 characters"
        }

        return JwtKeys(
            accessSecretKey = SecretKeySpec(
                accessSecret.toByteArray(StandardCharsets.UTF_8),
                "HmacSHA256"
            ),
            refreshSecretKey = SecretKeySpec(
                refreshSecret.toByteArray(StandardCharsets.UTF_8),
                "HmacSHA256"
            )
        )
    }

    @Bean
    fun jwtEncoders(keys: JwtKeys): JwtEncoders {
        return JwtEncoders(
            accessJwtEncoder = accessJwtEncoder(keys),
            refreshJwtEncoder = refreshJwtEncoder(keys)
        )
    }

    @Bean
    fun accessReactiveJwtDecoder(keys: JwtKeys): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder
            .withSecretKey(keys.accessSecretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }

    private fun accessJwtEncoder(keys: JwtKeys): JwtEncoder {
        val jwk = OctetSequenceKey.Builder(keys.accessSecretKey).build()
        return NimbusJwtEncoder(ImmutableJWKSet(JWKSet(jwk)))
    }

    private fun refreshJwtEncoder(keys: JwtKeys): JwtEncoder {
        val jwk = OctetSequenceKey.Builder(keys.refreshSecretKey).build()
        return NimbusJwtEncoder(ImmutableJWKSet(JWKSet(jwk)))
    }
}