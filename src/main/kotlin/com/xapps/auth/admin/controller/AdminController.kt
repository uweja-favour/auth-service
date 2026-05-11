//package com.xapps.auth.admin.controller
//
//import com.xapps.auth.persistence.repository.MonthlyResetRepository
//import com.xapps.auth.persistence.repository.PaymentRepository
//import com.xapps.auth.infrastructure.repository.user.UserRepository
//import org.slf4j.LoggerFactory
//import org.springframework.core.io.ByteArrayResource
//import org.springframework.http.HttpHeaders
//import org.springframework.http.MediaType
//import org.springframework.http.ResponseEntity
//import org.springframework.security.core.Authentication
//import org.springframework.web.bind.annotation.*
//import java.nio.charset.StandardCharsets
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
//@RestController
//@RequestMapping("/admin")
//class AdminController(
//    private val monthlyResetRepository: MonthlyResetRepository,
////    private val subscriptionService: SubscriptionService,
//    private val userRepository: UserRepository,
//    private val paymentRepository: PaymentRepository
//) {
//    private val logger = LoggerFactory.getLogger(AdminController::class.java)
//
//    // Securely checks the date of last system-wide reset
//    @GetMapping("/reset-status")
//    fun getResetStatus(authentication: Authentication): ResponseEntity<Any> {
//        if (!authentication.isAdmin()) return forbidden()
//
//        return try {
//            val reset = monthlyResetRepository.findById("singleton").orElse(null)
//                ?: return ResponseEntity.status(404).body("Reset status not found")
//
//            ResponseEntity.ok(mapOf("lastResetDate" to reset.lastReset.toString()))
//        } catch (e: Exception) {
//            logger.error("❌ Failed to retrieve reset status", e)
//            ResponseEntity.status(500).body("Failed to retrieve reset status")
//        }
//    }
//
//    // Resets all monthly counters (only once per month) and logs every edge case
//    @PostMapping("/reset-now")
//    suspend fun manualReset(authentication: Authentication): ResponseEntity<Any> {
//        if (!authentication.isAdmin()) return forbidden()
//
//        return try {
////            subscriptionService.checkAndResetIfNeeded()
//
//            logger.info("✅ Manual reset completed successfully by admin.")
//            ResponseEntity.ok("✅ Manual reset completed successfully.")
//        } catch (e: Exception) {
//            logger.error("❌ Manual reset failed", e)
//            ResponseEntity.status(500).body("Reset failed: ${e.message}")
//        }
//    }
//
//    // Fetch all registered users
//    @GetMapping("/users")
//    fun getAllUsers(auth: Authentication): ResponseEntity<Any> {
//        if (!auth.isAdmin()) return forbidden()
//
//        return try {
//            val users = userRepository.findAll()
//            ResponseEntity.ok(users)
//        } catch (e: Exception) {
//            logger.error("❌ Failed to fetch users", e)
//            ResponseEntity.status(500).body("Internal error fetching users")
//        }
//    }
//
//    // Ban a specific user by ID; prevents banning already banned or nonexistent users
//    @PostMapping("/ban")
//    fun banUser(
//        auth: Authentication,
//        @RequestParam userId: String
//    ): ResponseEntity<Any> {
//        if (!auth.isAdmin()) return forbidden()
//
//        return try {
//            val user = userRepository.findById(userId).orElse(null)
//                ?: return ResponseEntity.status(404).body("User not found")
//
//            if (user.isBanned) {
//                return ResponseEntity.status(409).body("User already banned")
//            }
//
//            user.isBanned = true
//            userRepository.createUser(user)
//
//            logger.info("🚫 User $userId has been banned by admin.")
//            ResponseEntity.ok("User $userId banned successfully")
//        } catch (e: Exception) {
//            logger.error("❌ Failed to ban user", e)
//            ResponseEntity.status(500).body("Failed to ban user")
//        }
//    }
//
//    @PostMapping("/activate-banned-user")
//    fun activateBannedUser(
//        auth: Authentication,
//        @RequestParam userId: String
//    ): ResponseEntity<Any> {
//        if (!auth.isAdmin()) return forbidden()
//
//        return try {
//            val user = userRepository.findById(userId).orElse(null)
//                ?: return ResponseEntity.status(404).body("User not found")
//
//            if (!user.isBanned) {
//                return ResponseEntity.status(409).body("User is not banned")
//            }
//
//            user.isBanned = false
//            userRepository.createUser(user)
//
//            logger.info("🚫 User $userId has been un-banned by admin.")
//            ResponseEntity.ok("User $userId un-banned successfully")
//        } catch (e: Exception) {
//            logger.error("Failed to un-ban user. User ID: $userId. Exception: ${e.message}")
//            ResponseEntity.status(500).body("Failed to activate banned user")
//        }
//    }
//
//    // Retrieve list of all active subscriptions
//    @GetMapping("/subscriptions")
//    fun getAllSubscriptions(auth: Authentication): ResponseEntity<Any> {
//        if (!auth.isAdmin()) return forbidden()
//
//        return try {
////            val subscriptions = subscriptionService.getAllSubscriptions()
//            ResponseEntity.ok("subscriptions")
//        } catch (e: Exception) {
//            logger.error("❌ Failed to fetch subscriptions", e)
//            ResponseEntity.status(500).body("Failed to fetch subscriptions")
//        }
//    }
//
//
//    @GetMapping("/export")
//    fun exportUsersAndPayments(auth: Authentication): ResponseEntity<Any> {
//        if (!auth.isAdmin()) return forbidden()
//
//        return try {
//            val users = userRepository.findAllStudents()
//            val payments = paymentRepository.findAll()
//
//            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//            logger.info("📦 Exporting ${users.size} users and ${payments.size} payments at $now")
//
//            val csvBuilder = StringBuilder()
//                .append("ADMIN DATA EXPORT — Generated at: $now\n\n")
//
//            // === USERS ===
//            csvBuilder.append("=== USERS (${users.size}) ===\n")
//                .append("ID,Name,Email,Banned Status,Subscription Platform,Plan,Stripe Customer ID,Last Plan Reset,Monthly Used Plan Tokens,Quiz Or App Tokens,Allowed Free Plan Allowance,Subscription Start,Subscription End\n")
//
//            users.forEach {
//                csvBuilder.append(
//                    listOf(
//                        it.id,
//                        safeCsv(it.username),
//                        safeCsv(it.email),
//                        if (it.isBanned) "BANNED" else "ACTIVE",
//                        it.subscriptionPlatform.name,
//                        it.plan.name,
//                        it.stripeCustomerId.toString(),
//                        it.lastPlanReset?.toString() ?: "N/A",
//                        it.monthlyUsedPlanTokens.toString(),
//                        it.quizTokens.toString(),
//                        it.canUseFreePlanAllowance.toString(),
//                        it.subscriptionStart?.toString() ?: "N/A",
//                        it.subscriptionEnd?.toString() ?: "N/A"
//                    ).joinToString(",")
//                ).append("\n")
//            }
//
//            if (users.isEmpty()) csvBuilder.append("[NO STUDENTS FOUND]\n")
//
//            csvBuilder.append("\n")
//
//            // === PAYMENTS ===
//            csvBuilder.append("=== PAYMENTS (${payments.size}) ===\n")
//                .append("Payment ID,User ID,User Email,Amount,Date,Subscription Platform,Transaction ID\n")
//
//            payments.forEach { payment ->
//                val user = users.find { it.id == payment.userId }
//                csvBuilder.append(
//                    listOf(
//                        payment.id,
//                        payment.userId,
//                        safeCsv(user?.email ?: "UNKNOWN"),
//                        payment.amount.toString(),
//                        payment.paidAt.toString(),
//                        safeCsv(payment.platform.name),
//                        safeCsv(payment.transactionId ?: "N/A")
//                    ).joinToString(",")
//                ).append("\n")
//            }
//
//            if (payments.isEmpty()) csvBuilder.append("[NO PAYMENTS FOUND]\n")
//
//            val csvData = csvBuilder.toString().toByteArray(StandardCharsets.UTF_8)
//            val resource = ByteArrayResource(csvData)
//
//            ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("text/csv"))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=admin-report.csv")
//                .body(resource)
//
//        } catch (e: Exception) {
//            logger.error("❌ Failed to export admin CSV report", e)
//            return ResponseEntity.status(500).body("Export failed: ${e.message}")
//        }
//    }
//
//    // Reusable forbidden response for clarity
//    private fun forbidden(): ResponseEntity<Any> =
//        ResponseEntity.status(403).body("Access denied")
//
//    // Helper: Check if the current user is an ADMIN
//    private fun Authentication.isAdmin(): Boolean =
//        this.authorities.any { it.authority == "ROLE_ADMIN" }
//
//
//    private fun safeCsv(value: String?): String {
//        return value?.replace("\"", "\"\"")?.let {
//            if (it.contains(",") || it.contains("\n")) "\"$it\"" else it
//        } ?: ""
//    }
//
//}
