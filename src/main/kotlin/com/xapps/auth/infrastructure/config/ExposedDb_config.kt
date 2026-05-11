//@file:OptIn(ExperimentalTime::class)
//
//package com.xapps.auth.infrastructure.config
//
//import io.r2dbc.spi.ConnectionFactory
//import io.r2dbc.spi.IsolationLevel
//import kotlinx.coroutines.Dispatchers
//import org.jetbrains.exposed.v1.core.transactions.TransactionManagerApi
//import org.jetbrains.exposed.v1.core.vendors.MysqlDialect
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
//import org.jetbrains.exposed.v1.r2dbc.transactions.TransactionManager
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import kotlin.apply
//import kotlin.time.ExperimentalTime
//
//@Configuration
//class ExposedR2dbcConfig(
//    // Spring Boot will provide this if you have spring.r2dbc.url (and r2dbc driver) configured
//    private val connectionFactory: ConnectionFactory
//) {
//
//    @Bean
//    fun r2dbcDatabase(): R2dbcDatabase {
//        val builder = R2dbcDatabaseConfig.Builder().apply {
//            // optional but recommended if dialect resolution might fail — set explicit dialect
//            explicitDialect = MysqlDialect()
//
//            // set sensible defaults
//            defaultR2dbcIsolationLevel = IsolationLevel.READ_COMMITTED
//            dispatcher = Dispatchers.IO
//
//            // you can set more builder properties here (fetchSize, typeMapping, etc)
//        }
//
//        // pass the ConnectionFactory + the built config, and use Exposed's R2DBC TransactionManager
//        return R2dbcDatabase.connect(
//            connectionFactory = connectionFactory,
//            manager = { TransactionManager(it) },
//            databaseConfig = builder
////            connectionFactory,
////            databaseConfig = builder.build(),
////            manager = { TransactionManager(it) } // there is no TransactionManagerFactory; use the manager lambda
//        )
//    }
//
//    @Bean
//    fun exposedTransactionManager(r2dbcDatabase: R2dbcDatabase): TransactionManagerApi =
//        TransactionManager(r2dbcDatabase)
//}






// IF YOU WANT TO BUILD THE CONNECTION FACTORY YOURSELF:
//
//@Configuration
//class ManualR2dbcConfig {
//
//    @Bean
//    fun connectionFactory(): ConnectionFactory {
//        val options = ConnectionFactoryOptions.builder()
//            .option(ConnectionFactoryOptions.DRIVER, "postgresql")
//            .option(ConnectionFactoryOptions.HOST, "localhost")
//            .option(ConnectionFactoryOptions.PORT, 5432)
//            .option(ConnectionFactoryOptions.DATABASE, "selftest_quiz_dev")
//            .option(ConnectionFactoryOptions.USER, "postgres")
//            .option(ConnectionFactoryOptions.PASSWORD, "password")
//            .build()
//        return ConnectionFactories.get(options)
//    }
//
//    @Bean
//    fun r2dbcDatabase(connectionFactory: ConnectionFactory): R2dbcDatabase {
//        val config = R2dbcDatabaseConfig.Builder().apply {
//            // either set connectionFactoryOptions (builder helper) or leave it if you're passing the CF directly
//            connectionFactoryOptions {
//                option(ConnectionFactoryOptions.DRIVER, "postgresql")
//                option(ConnectionFactoryOptions.HOST, "localhost")
//                option(ConnectionFactoryOptions.PORT, 5432)
//                option(ConnectionFactoryOptions.DATABASE, "selftest_quiz_dev")
//                option(ConnectionFactoryOptions.USER, "postgres")
//                option(ConnectionFactoryOptions.PASSWORD, "password")
//            }
//
//            explicitDialect = PostgreSQLDialect()
//            defaultR2dbcIsolationLevel = IsolationLevel.READ_COMMITTED
//            dispatcher = Dispatchers.IO
//        }.build()
//
//        return R2dbcDatabase.connect(connectionFactory, databaseConfig = config, manager = { TransactionManager(it) })
//    }
//
//    @Bean
//    fun exposedTransactionManager(r2dbcDatabase: R2dbcDatabase) = TransactionManager(r2dbcDatabase)
//}
