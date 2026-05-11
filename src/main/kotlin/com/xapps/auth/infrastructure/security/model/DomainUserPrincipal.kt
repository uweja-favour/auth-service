package com.xapps.auth.infrastructure.security.model

import com.xapps.auth.domain.model.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class DomainUserPrincipal(
    val user: User,
    val email: String,
    val userRole: UserRole,
    val otherRoles: List<String> = emptyList()
) : UserDetails {

    val userId get() = user.userId

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return (
            otherRoles.map { SimpleGrantedAuthority("ROLE_$it") } +
            SimpleGrantedAuthority("ROLE_$userRole")
        ).distinct()
    }

    override fun getPassword(): String? = null
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

@JvmInline
value class UserRoleCode(val value: String)

enum class UserRole(val code: UserRoleCode) {
    ADMIN(UserRoleCode("admin")),
    USER(UserRoleCode("user"));

    companion object {
        private val byCodes = entries.associateBy { it.code }

        fun fromCode(code: UserRoleCode): UserRole {
            return byCodes[code] ?: throw IllegalArgumentException("Unknown user role code $code")
        }
    }
}