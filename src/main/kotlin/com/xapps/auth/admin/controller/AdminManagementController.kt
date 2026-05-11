//package com.xapps.auth.admin.controller
//
//import com.xapps.auth.domain.entity.UserRole
//import com.xapps.auth.domain.entity.withIdentifyingInfo
//import com.xapps.auth.admin.dto.AlterUserRoleRequest
//import com.xapps.auth.infrastructure.repository.user.UserRepository
//import jakarta.validation.Valid
//import org.slf4j.LoggerFactory
//import org.springframework.http.ResponseEntity
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.core.Authentication
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/api/admin/manage")
//class AdminManagementController(
//    private val userRepo: UserRepository
//) {
//    private val logger = LoggerFactory.getLogger(AdminManagementController::class.java)
//
//    /**
//     * Promote a user to ADMIN.
//     * Only accessible by users with ROLE_ADMIN.
//     * Failure resistant and thoroughly validated.
//     */
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/promote-user")
//    fun promoteUser(
//        @Valid @RequestBody request: AlterUserRoleRequest,
//        authentication: Authentication
//    ): ResponseEntity<Any> {
//        return alterUserRole("promote", request, authentication)
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("demote-user")
//    fun demoteUser(
//        authentication: Authentication,
//        @Valid @RequestBody request: AlterUserRoleRequest
//    ): ResponseEntity<Any> {
//        return alterUserRole("demote", request, authentication)
//    }
//
//    private fun alterUserRole(
//        action: String,
//        request: AlterUserRoleRequest,
//        authentication: Authentication
//    ): ResponseEntity<Any> {
//        if (!authentication.isAdmin()) return forbidden()
//
//        val user = userRepo.findByUserId(request.email)
//            ?: return ResponseEntity.status(404).body("User not found: ${request.email}")
//
//        val newRole = try {
//            UserRole.valueOf(request.newRole.uppercase())
//        } catch (e: Exception) {
//            return ResponseEntity.badRequest().body("New role: ${request.newRole} not found.")
//        }
//
//        if (user.role == newRole) {
//            return ResponseEntity.badRequest().body("User ${user.withIdentifyingInfo()} already has role: ${request.newRole}")
//        }
//
//        user.role = newRole
//
//        userRepo.createUser(user)
//        logger.info("User ${user.withIdentifyingInfo()} $action to ${newRole.name}")
//        return ResponseEntity.ok("✅ User ${user.withIdentifyingInfo()} $action to ${newRole.name}")
//    }
//
//    private fun Authentication.isAdmin(): Boolean =
//        this.authorities.any { it.authority == "ROLE_ADMIN" }
//
//    // Reusable forbidden response for clarity
//    private fun forbidden(): ResponseEntity<Any> =
//        ResponseEntity.status(403).body("Access denied")
//
//}
