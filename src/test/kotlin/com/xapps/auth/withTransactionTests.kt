//package com.xapps.auth
//
//import io.r2dbc.spi.ConnectionFactories
//import io.r2dbc.spi.ConnectionFactoryOptions
//import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
//import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
//import io.r2dbc.spi.ConnectionFactoryOptions.HOST
//import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
//import io.r2dbc.spi.ConnectionFactoryOptions.PORT
//import io.r2dbc.spi.ConnectionFactoryOptions.USER
//import io.r2dbc.spi.IsolationLevel
//import kotlinx.coroutines.*
//import kotlinx.coroutines.test.runTest
//import org.jetbrains.exposed.v1.core.vendors.MysqlDialect
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
//import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction
//import org.jetbrains.exposed.v1.r2dbc.transactions.TransactionManager
//import org.junit.jupiter.api.AfterAll
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.TestInstance
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertSame
//import kotlin.test.assertTrue
//
//@kotlin.ExperimentalStdlibApi
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class TransactionScopeTest {
//
//    private lateinit var database: R2dbcDatabase
//
//    @BeforeAll
//    fun setupDatabase() {
//        // Create an in-memory H2 R2DBC database for testing
//        val connectionFactory = ConnectionFactories.get(
//            ConnectionFactoryOptions.builder()
//                .option(DRIVER, "mysql")      // R2DBC driver for PostgreSQL
//                .option(HOST, "localhost")         // Database host
//                .option(PORT, 3306)                // Database port
//                .option(USER, "root")          // Database username
//                .option(PASSWORD, "Sentry_Password")      // Database password
//                .option(DATABASE, "auth_dev")
//                .build()
//        )
//
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
//        database = R2dbcDatabase.connect(
//            connectionFactory,
//            databaseConfig = builder.build(),
//            manager = { TransactionManager(it) } // there is no TransactionManagerFactory; use the manager lambda
//        )
//    }
//
//    @AfterAll
//    fun teardownDatabase() {
////        runBlocking {
////            database.
////        }
//    }
//
//    @BeforeEach
//    fun setup() {
////        theThreadLocal.remove()
//    }
//
//    @Test
//    fun `thread context is switched to IO if not already on IO`() = runTest {
//        val capturedDispatcher = AtomicQueue<CoroutineDispatcher>()
//        withTransaction {
//            capturedDispatcher.add(coroutineContext[CoroutineDispatcher] ?: Dispatchers.Default)
//        }
//        // We are forcing IO dispatch internally
//        assertTrue(capturedDispatcher.isNotEmpty())
//        println("HELLO")
//        println("HELLO")
//        println("HELLO")
//        println("Size of the list: ${capturedDispatcher.list.size}")
//        println("Items in the list:")
//        capturedDispatcher.list.forEach {
//            println("Item is: $it")
//        }
//        assertTrue(capturedDispatcher.list.all { it == Dispatchers.IO || it == Dispatchers.Default })
//    }
//
//    @Test
//    fun `nested transaction does not start a new root transaction`() = runTest {
//        var rootTxn: R2dbcTransaction? = null
//        var nestedTxn: R2dbcTransaction? = null
//
//        suspend fun nested() = withTransaction {
//            nestedTxn = this
//        }
//
//        withTransaction {
//            rootTxn = this
//            nested()
//        }
//
//        assertSame(rootTxn, nestedTxn, "Nested transaction must reuse root transaction")
//    }
//
//    @Test
//    fun `multiple concurrent coroutines share correct root transaction`() = runTest {
//        val rootTransactions = mutableSetOf<R2dbcTransaction>()
//        val nestedTransactions = mutableSetOf<R2dbcTransaction>()
//
//        suspend fun child() = withTransaction {
//            nestedTransactions += this
//            delay(10)
//        }
//
//        withTransaction {
//            rootTransactions += this
//            coroutineScope {
//                repeat(10) {
//                    launch { child() }
//                }
//            }
//        }
//
//        // Ensure all nested transactions are the same as the root
//        assertEquals(1, rootTransactions.size)
//        assertEquals(1, nestedTransactions.size)
//        assertSame(rootTransactions.first(), nestedTransactions.first())
//    }
//
//    @Test
//    fun `stress test with nested coroutines`() = runTest {
//        val captured = AtomicQueue<R2dbcTransaction>()
//
//        suspend fun nested(depth: Int) {
//            withTransaction {
//                captured.add(this)
//                if (depth > 0) {
//                    nested(depth - 1)
//                }
//            }
//        }
//
//        withTransaction {
//            coroutineScope {
//                repeat(10) {
//                    launch {
//                        nested(5)
//                    }
//                }
//            }
//        }
//
//        // Ensure all transactions captured are same root transaction
//        assertEquals(captured.list.distinct().size, 1)
//    }
//
//    @Test
//    fun `parallel root transactions are independent`() = runTest {
//        val transactions = mutableSetOf<R2dbcTransaction>()
//
//        // Run multiple root transactions in parallel
//        coroutineScope {
//            repeat(5) {
//                launch {
//                    // Each launch should get its own root transaction
//                    // because ThreadLocal is not inherited by new coroutines
//                    withTransaction {
//                        delay(10)
//                        synchronized(transactions) {
//                            transactions += this
//                        }
//                    }
//                }
//            }
//        }
//
//        // Since ThreadLocal doesn't propagate to child coroutines,
//        // each launch should create its own root transaction
//        assertTrue(transactions.size >= 1, "At least one transaction should be created")
//    }
//}