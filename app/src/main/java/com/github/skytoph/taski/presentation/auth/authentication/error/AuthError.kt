package com.github.skytoph.taski.presentation.auth.authentication.error

import com.github.skytoph.taski.presentation.core.state.StringResource

sealed class AuthError(open val errorMessage: StringResource) {

    class PasswordError(override val errorMessage: StringResource) : AuthError(errorMessage)

    class EmailError(override val errorMessage: StringResource) : AuthError(errorMessage)

    class GeneralError(override val errorMessage: StringResource) : AuthError(errorMessage)
}