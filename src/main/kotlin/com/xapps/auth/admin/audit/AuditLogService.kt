//package com.xapps.studentapplication.admin.audit
//
//import com.xapps.auth.admin.audit.AuditLog
//import com.xapps.auth.admin.repository.AuditLogRepository
//import com.xapps.auth.admin.audit.LogType
//import com.xapps.studentapplication.entity.User
//import org.springframework.stereotype.Service
//
//
//import org.slf4j.LoggerFactory
//import java.time.LocalDateTime
//
//@Service
//class AuditLogService(
//    private val auditRepo: AuditLogRepository
//) {
//    private val logger = LoggerFactory.getLogger(AuditLogService::class.java)
//
//    /**
//     * Records a Stripe webhook failure in the audit log table.
//     * If the DB write fails, it logs a fallback to error logs.
//     *
////     * @param sessionId Stripe session ID related to the error (required)
////     * @param userEmail The user's email involved in the failure (required)
//     * @param reason The detailed failure reason (required)
//     */
//
//    fun recordStripeFailure(
//        reason: String,
//        metadata: Map<String, String>
//    ) {
//        val sessionId = metadata["sessionId"]
//        val user = metadata["user"] ?: metadata["email"] ?: "unknown@user_or_userEmail"
//
//        try {
//            val auditLog = AuditLog(
//                timestamp = LocalDateTime.now(),
//                type = LogType.STRIPE_ERROR,
//                message = reason.trim(),
//                metadata = metadata
//            )
//
//            auditRepo.save(auditLog)
//            logger.info("✅ Stripe error audit log saved for session: $sessionId, user: $user")
//        } catch (e: Exception) {
//            logger.error(
//                "🚨 Failed to save Stripe error audit log for session: $sessionId, user: $user. " +
//                        "Reason: $reason. Metadata: $metadata. Exception: ${e.message}", e
//            )
//        }
//    }
//
//    /**
//     * Records a GooglePlay subscription webhook failure in the audit log table.
//     * If the DB write fails, it logs a fallback to error logs.
//     *
//     * @param purchaseToken GooglePlay purchase Token related to the error (required)
//     * @param user The user involved in the failure (required)
//     * @param reason The detailed failure reason (required)
//     */
//    fun recordPlaySubscriptionFailure(
//        purchaseToken: String,
//        subscriptionId: String,
//        packageName: String,
//        user: User?,
//        userEmail: String?,
//        reason: String
//    ) {
//        try {
//            val auditLog = AuditLog(
//                timestamp = LocalDateTime.now(),
//                type = LogType.GOOGLE_PLAY_SUBSCRIPTION_ERROR,
//                message = reason.trim(),
//                metadata = mapOf(
//                    "purchaseToken" to purchaseToken,
//                    "subscriptionId" to subscriptionId,
//                    "packageName" to packageName,
//                    "user" to user.toString(),
//                    "email" to (user?.email ?: userEmail ?: "unknown@userEmail")
//                )
//            )
//
//            auditRepo.save(auditLog)
//            logger.info("✅ Google Play audit log saved for token: $purchaseToken, user: $user")
//        } catch (e: Exception) {
//            logger.error(
//                "🚨 Failed to save Play audit log for token: $purchaseToken, user: $user. " +
//                        "Reason: $reason. Exception: ${e.message}", e
//            )
//        }
//    }
//
//    fun recordGenericFailure(
//        type: LogType,
//        message: String,
//        metadata: Map<String, Any> = emptyMap()
//    ) {
//        try {
//            val safeMetadata = metadata
//                .mapValues { it.value.toString().trim() }
//
//            auditRepo.save(
//                AuditLog(
//                    timestamp = LocalDateTime.now(),
//                    type = type,
//                    message = message.trim(),
//                    metadata = safeMetadata
//                )
//            )
//            logger.info("📋 Audit log recorded — [$type]: $message")
//
//        } catch (e: Exception) {
//            logger.error("🚨 Critical: Failed to record audit log: ${e.message}. Metadata: $metadata, type: $type, message: $message", e)
//        }
//    }
//
//    fun logTransaction(
//        userId: String,
//        userEmail: String,
//        eventType: String,
//        planName: String?,
//        amount: Double?,
//        subscriptionType: String,
//        transactionId: String,
//        status: String, // e.g. "activated", "renewed", "cancelled"
//        metadata: Map<String, String> = emptyMap()
//    ) {
//        val log = AuditLog(
//            type = LogType.STRIPE_ACTIVITY,
//            message = buildString {
//                appendLine("Title: $subscriptionType Subscription ${status.replaceFirstChar { it.uppercase() }}")
//                appendLine("Message: ")
//                appendLine("✅ $subscriptionType subscription was $status successfully.")
//                appendLine("• User ID: $userId")
//                appendLine("• Email: $userEmail")
//                appendLine("• Event Type: $eventType")
//                appendLine("• Plan: ${planName ?: "Unknown"}")
//                appendLine("• Amount: ${amount?.toString() ?: "Unknown"}")
//                appendLine("• Transaction ID: $transactionId")
//
//                if (metadata.isNotEmpty()) {
//                    appendLine("• Additional Metadata:")
//                    metadata.forEach { (k, v) -> appendLine("  - $k: $v") }
//                }
//            },
//            metadata = metadata + mapOf(
//                "userId" to userId,
//                "email" to userEmail,
//                "eventType" to eventType,
//                "plan" to (planName ?: "unknown"),
//                "amount" to (amount?.toString() ?: "unknown"),
//                "transactionId" to transactionId,
//                "status" to status
//            )
//        )
//
//        runCatching {
//            auditRepo.save(log)
//        }.onFailure {
//            logger.error("🔥 Failed to persist $subscriptionType subscription log transaction. The subscription log: $log", it)
//        }
//    }
//
//    fun record(
//        title: String,
//        reason: String,
//        logType: LogType,
//        metadata: Map<String, String>
//    ) {
//        try {
//
//            auditRepo.save(
//                AuditLog(
//                    timestamp = LocalDateTime.now(),
//                    type = logType,
//                    message = reason.trim(),
//                    metadata = metadata
//                )
//            )
//            logger.info("📋 Audit log recorded — [$logType]: $reason")
//
//        } catch (e: Exception) {
//            logger.error("🚨 Critical: Failed to record audit log: ${e.message}. Metadata: $metadata, type: $logType, message: $reason", e)
//        }
//    }
//}