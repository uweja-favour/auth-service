//package com.xapps.auth.infrastructure.config
//
//import io.r2dbc.spi.ConnectionFactories
//import io.r2dbc.spi.ConnectionFactory
//import io.r2dbc.spi.ConnectionFactoryOptions
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
//import io.r2dbc.spi.ConnectionFactoryOptions.*
//
///**
// * Configuration class for R2DBC (Reactive Relational Database Connectivity) with PostgreSQL.
// * Sets up a reactive ConnectionFactory for use with Spring Data R2DBC repositories.
// */
//@Configuration
//@EnableR2dbcRepositories // Enables scanning of Spring Data R2DBC repositories
//class R2dbcConfig : AbstractR2dbcConfiguration() {
//
//    /**
//     * Defines the ConnectionFactory bean that provides reactive DB connections.
//     * This factory is used internally by Spring Data R2DBC to run queries.
//     */
//    @Bean
//    override fun connectionFactory(): ConnectionFactory {
//        return ConnectionFactories.get(
//            ConnectionFactoryOptions.builder()
//                .option(DRIVER, "mysql")      // R2DBC driver for PostgreSQL
//                .option(HOST, "localhost")         // Database host
//                .option(PORT, 3306)                // Database port
//                .option(USER, "root")          // Database username
//                .option(PASSWORD, "Sentry_Password")      // Database password
//                .option(DATABASE, "auth_dev")      // Database name (matches application-dev.yml)
//                .build()
//        )
//    }
//}
