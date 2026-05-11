//package com.xapps.auth.infrastructure.config
//
//import jakarta.annotation.PostConstruct
//import kotlinx.coroutines.runBlocking
//import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
//import org.springframework.stereotype.Component
//
//@Component
//class DatabaseInitializer(
//    private val database: R2dbcDatabase
//) {
//    @PostConstruct
//    fun init() = runBlocking {
//        // THIS RUNS EXACTLY ONCE (1) ON AN EMPTY DATABASE
////        suspendTransactionAsync(db = database) {
////            SchemaUtils.create(
////                FcmDeviceEntity,
////                UserEntity,
////                UserProfileEntity,
////                UserSubscriptionEntity,
////                AccessTokenEntity,
////                MonthlyResetEntity,
////                NotificationEntity,
////                PaymentEntity,
////                RefreshTokenEntity,
////                UsedGooglePlaySubscriptionPurchaseTokenEntity
////            )
////        }.await()
//    }
//}
