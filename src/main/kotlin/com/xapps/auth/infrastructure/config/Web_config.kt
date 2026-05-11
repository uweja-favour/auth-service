package com.xapps.auth.infrastructure.config

import com.xapps.auth.core.web.ClientMetadataArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

/**
 * Spring WebFlux configuration class for customizing MVC/WebFlux behavior.
 *
 * In this case, it adds a custom argument resolver so controller methods
 * can automatically receive client metadata without manually extracting it from the request.
 */
@Configuration
class WebConfig(
    private val clientMetadataResolver: ClientMetadataArgumentResolver // Custom argument resolver
) : WebFluxConfigurer {

    /**
     * Registers custom argument resolvers with Spring WebFlux.
     *
     * @param configurer The object used to add custom method argument resolvers.
     */
    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        // Add our custom ClientMetadataArgumentResolver to automatically inject client info
        configurer.addCustomResolver(clientMetadataResolver)
    }
}
