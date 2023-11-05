package com.github.skytoph.taski.presentation.auth.authentication

import com.github.skytoph.taski.presentation.auth.authentication.error.AuthError
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData

data class AuthState(
    val isSignInSuccessful: Boolean = false,
    val signInError: AuthError? = null
)

data class AuthResult(
    val user: UserData? = null,
    val error: AuthError? = null
)