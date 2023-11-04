package com.github.skytoph.taski.presentation.auth.signin.validation

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.signin.SignInEvent
import com.github.skytoph.taski.presentation.auth.signin.SignInEventHandler
import com.github.skytoph.taski.presentation.auth.signin.SignInState
import com.github.skytoph.taski.presentation.auth.validation.AuthValidator
import com.github.skytoph.taski.presentation.auth.validation.EmptinessValidator

class SignInValidator : AuthValidator(
    passwordValidator = EmptinessValidator(null, R.string.error_password_is_empty),
    emailValidator = EmptinessValidator(null, R.string.error_email_is_invalid),
) {

    fun validate(state: SignInState, eventHandler: SignInEventHandler) {
        val emailErrorResId = validateEmail(state.email.field)
        eventHandler.onEvent(SignInEvent.EmailError(emailErrorResId))

        val passwordErrorResId = validatePassword(state.password.field)
        eventHandler.onEvent(SignInEvent.PasswordError(passwordErrorResId))

        if (passwordErrorResId == null && emailErrorResId == null)
            eventHandler.onEvent(SignInEvent.Validate(true))
    }
}