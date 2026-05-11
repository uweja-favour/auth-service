package com.xapps.auth.domain.exceptions

/**
 * This MUST only be called from the Auth Service attempt to login a user
 */
class EmailNotFoundException (val email: String) : IllegalStateException("Email not found: $email")