package com.github.skytoph.taski.presentation.auth.signin.mapper

import com.github.skytoph.taski.presentation.auth.authentication.error.AuthError
import com.github.skytoph.taski.presentation.auth.signin.SignInEvent

fun AuthError.map(): SignInEvent = when (this) {
    is AuthError.EmailError -> SignInEvent.EmailError(this.errorMessage)
    is AuthError.PasswordError -> SignInEvent.PasswordError(this.errorMessage)
    is AuthError.GeneralError -> SignInEvent.Error(this.errorMessage)
}
