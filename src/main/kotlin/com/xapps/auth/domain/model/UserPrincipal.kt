//package com.xapps.auth.domain.model
//
//import com.xapps.auth.domain.model.user.User
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.UserDetails
//
//class UserPrincipal(
//    private val user: User
//) : UserDetails {
//
//    override fun getAuthorities(): Collection<GrantedAuthority> {
//        return listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
//    }
//
//    override fun getPassword(): String = user.passwordHash
//
//    override fun getUsername(): String = user.email
//
//    override fun isAccountNonExpired(): Boolean = true
//
//    override fun isAccountNonLocked(): Boolean = !user.isBanned
//
//    override fun isCredentialsNonExpired(): Boolean = true
//
//    override fun isEnabled(): Boolean = true
//
//    fun getUser(): User = user
//}
