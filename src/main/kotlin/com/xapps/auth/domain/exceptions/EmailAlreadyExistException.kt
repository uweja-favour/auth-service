package com.xapps.auth.domain.exceptions

class EmailAlreadyExistException (val email: String) : RuntimeException("Email already exist: $email")