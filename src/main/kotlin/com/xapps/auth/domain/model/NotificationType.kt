package com.xapps.auth.domain.model;

@JvmInline
value class NotificationTypeCode(val value: String)

enum class NotificationType(val code: NotificationTypeCode, val title: String, val defaultMessage: String) {

    SYSTEM(
        code = NotificationTypeCode("system"),
        title = "System Update",
        defaultMessage = "Important system notification. Please check the details."
    ),

    SIGN_UP(
        code = NotificationTypeCode("sign_up"),
        title = "Welcome!",
        defaultMessage = "Thank you for signing up. We’re excited to have you on board!"
    ),

    BACK_UP_SUCCESSFUL(
        code = NotificationTypeCode("back_up_successful"),
        title = "Back up successful",
        defaultMessage = "Your data has been backed up successfully."
    ),

    SUB_UPDATE(
        code = NotificationTypeCode("sub_update"),
        title = "Subscription Update",
        defaultMessage = "Your subscription details have been updated."
    ),

    SUB_RENEWAL(
        code = NotificationTypeCode("sub_renewal"),
        title = "Subscription Renewed",
        defaultMessage = "Your subscription has been renewed successfully."
    ),

    SUB_ACTIVATED(
        code = NotificationTypeCode("sub_activated"),
        title = "Subscription Activated",
        defaultMessage = "Your subscription has been activated successfully."
    ),

    SUB_CANCELLATION(
        code = NotificationTypeCode("sub_cancelation"),
        title = "Subscription Cancelled",
        defaultMessage = "Your subscription has been cancelled. We’re sorry to see you go."
    ),

    SIGN_IN(
        code = NotificationTypeCode("sign_in"),
        title = "Welcome Back",
        defaultMessage = "You have signed in successfully."
    ),

    ALERT(
        code = NotificationTypeCode("alert"),
        title = "Urgent Alert",
        defaultMessage = "Please review this alert immediately."
    ),

    INFO(
        code = NotificationTypeCode("info"),
        title = "Information",
        defaultMessage = "Here’s some information for you."
    ),

    PROMOTION(
        code = NotificationTypeCode("promotion"),
        title = "Special Promotion",
        defaultMessage = "Check out this exclusive offer just for you!"
    );


    companion object {
        private val byCodes = entries.associateBy { it.code }

        fun fromCode(code: NotificationTypeCode): NotificationType {
            return byCodes[code] ?: throw IllegalArgumentException("Unknown notification type code: $code")
        }
    }
}
