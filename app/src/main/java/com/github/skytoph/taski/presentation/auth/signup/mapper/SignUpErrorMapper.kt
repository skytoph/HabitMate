package com.github.skytoph.taski.presentation.auth.signup.mapper

import com.github.skytoph.taski.presentation.auth.authentication.error.AuthError
import com.github.skytoph.taski.presentation.auth.signup.SignUpEvent

fun AuthError.map(): SignUpEvent = when (this) {
    is AuthError.EmailError -> SignUpEvent.EmailError(this.errorMessage)
    is AuthError.PasswordError -> SignUpEvent.PasswordError(this.errorMessage)
    is AuthError.GeneralError -> SignUpEvent.Error(this.errorMessage)
}