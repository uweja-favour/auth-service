package com.xapps.auth.admin.audit
//
//import com.xapps.studentapplication.admin.audit.JsonMapConverter
//import jakarta.persistence.*
//import java.time.LocalDateTime
//import java.util.*
//
//@Entity
//data class AuditLog(
//    @Id
//    val id: String = UUID.randomUUID().toString(),
//
//    val timestamp: LocalDateTime = LocalDateTime.now(),
//
//    @Enumerated(EnumType.STRING)
//    val type: LogType,
//
//    val message: String,
//
//    @Convert(converter = JsonMapConverter::class)
//    val metadata: Map<String, String> = emptyMap()
//)
//
//
//enum class LogType {
//    STRIPE_ERROR,
//    GOOGLE_PLAY_SUBSCRIPTION_ERROR,
//    SYSTEM_FAILURE,
//    AUTH_FAILURE,
//    ADMIN_ACTION,
//    INFO_EVENT,
//    STRIPE_ACTIVITY,
//    REFUND_SUCCESS
//}
