package com.xapps.auth.infrastructure.security.token

import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64

@Component
class JtiFactory {

    private val random = SecureRandom()

    fun createNewJti(): String =
        Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(
                ByteArray(16).also(random::nextBytes)
            )
}