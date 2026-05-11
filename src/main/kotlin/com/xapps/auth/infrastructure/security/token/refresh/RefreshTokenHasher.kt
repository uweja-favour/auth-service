package com.xapps.auth.infrastructure.security.token.refresh

import com.xapps.auth.infrastructure.security.jwt.JwtKeys
import org.springframework.stereotype.Component
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class RefreshTokenHasher {

    companion object {
        private const val HMAC_SHA_256 = "HmacSHA256"
    }

    fun hash(token: String, secretKey: SecretKey): String =
        Mac.getInstance(HMAC_SHA_256).run {
            init(secretKey)
            doFinal(token.toByteArray())
                .joinToString("") { "%02x".format(it) }
        }
}