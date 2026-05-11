package com.xapps.auth.core.web

/**
 * Annotation to mark a controller method parameter
 * that should be populated with ClientMetadata.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ClientInfo
