package com.xapps.auth.core.web

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * A custom Spring WebFlux argument resolver.
 * Automatically injects ClientMetadata into controller method parameters
 * annotated with @ClientInfo.
 */
@Component
class ClientMetadataArgumentResolver : HandlerMethodArgumentResolver {

    /**
     * Checks if this resolver supports the given method parameter.
     * - Parameter type must be ClientMetadata
     * - Parameter must be annotated with @ClientInfo
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == ClientMetadata::class.java &&
                parameter.hasParameterAnnotation(ClientInfo::class.java)
    }

    /**
     * Resolves the argument by extracting client metadata from the request.
     * Returns a Mono<ClientMetadata> to support reactive WebFlux controllers.
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        return Mono.just(ClientMetadataExtractor.extract(exchange))
    }
}
