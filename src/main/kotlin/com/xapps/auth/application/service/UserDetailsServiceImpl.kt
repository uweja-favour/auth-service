//package com.xapps.auth.application.service
//
//import com.xapps.auth.infrastructure.repository.user.UserRepository
//import com.xapps.auth.infrastructure.security.model.DomainUserPrincipal
//import kotlinx.coroutines.reactor.mono
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UsernameNotFoundException
//import org.springframework.stereotype.Service
//import reactor.core.publisher.Mono
//
//@Service
//class UserDetailsServiceImpl(
//    private val userRepository: UserRepository
//) : ReactiveUserDetailsService {
//
//    override fun findByUsername(email: String): Mono<UserDetails> =
//        mono {
//            val user = userRepository.findByEmail(email)
//                ?: throw UsernameNotFoundException("User not found with email: $email")
//
//            DomainUserPrincipal(
//                user = user,
//                email = user.email,
//                userRole = user.role,
//            )
//        }
//}