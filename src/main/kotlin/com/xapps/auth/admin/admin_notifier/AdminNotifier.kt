//package com.xapps.auth.admin.admin_notifier
//
//import com.xapps.studentapplication.admin.audit.AuditLogService
//import com.xapps.auth.admin.audit.LogType
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Component
//import java.time.LocalDateTime
//import java.util.concurrent.atomic.AtomicBoolean
//

//@Component
//class AdminNotifier(
////    private val fcmService: FCMService,
//    private val auditLogService: AuditLogService
//) {
//    private val logger = LoggerFactory.getLogger(AdminNotifier::class.java)
//
//    private val hasWarnedAboutFCM = AtomicBoolean(false)
//
//    fun notifyAdmin(
//        title: String,
//        message: String,
//        priority: Priority = Priority.NORMAL,
//        logType: LogType,
//        metadata: Map<String, Any> = emptyMap()
//    ) {
//        val finalTitle = "[${priority.name}]. The error is of logType: $logType. Title: $title".take(1000)
//        val metadataWithTime = metadata + mapOf(
//            "timestamp" to LocalDateTime.now().toString(),
//            "title" to finalTitle,
//            "priority" to priority.name,
//            "logType" to logType.name
//        )
//
//        // If the title or message is empty, record generic error and return.
//        if (title.isBlank() || message.isBlank()) {
//            logger.error("🚨 Admin notification skipped — blank title or message. Title='$title'. Message='$message'")
//            auditLogService.recordGenericFailure(
//                type = logType,
//                message = "Attempted to notify admin with blank title or message",
//                metadata = metadataWithTime
//            )
//            return
//        }
//
//        try {
////            fcmService.sendToAdminTopic(
////                title = finalTitle,
////                body = message,
////                priority = priority
////            )
//            logger.info("✅ Admin FCM notification sent: $finalTitle")
//        } catch (e: Exception) {
//            val shouldWarn = hasWarnedAboutFCM.compareAndSet(false, true)
//            if (shouldWarn) {
//                logger.error("🚨 First FCM failure detected. Suppressing future logs unless reset.", e)
//            } else {
//                logger.debug("FCM sendToAdminTopic failed silently: ${e.message}")
//            }
//
//            val relevantInfo = "The message that failed to be sent to Admins: $message. The title: $title. The metadataWithTime: $metadataWithTime"
//            // Fallback to audit log
//            try {
//                auditLogService.recordGenericFailure(
//                    type = logType,
//                    message = "FCM admin notification failed: ${e.message}. Relevant Info: $relevantInfo",
//                    metadata = metadataWithTime
//                )
//            } catch (auditEx: Exception) {
//                logger.error("🔥 CRITICAL: Failed to log to audit table after FCM failure. Admin will NOT be alerted. $relevantInfo", auditEx)
//            }
//        }
//    }
//}
