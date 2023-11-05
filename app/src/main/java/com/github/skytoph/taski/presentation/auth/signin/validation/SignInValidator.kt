package com.github.skytoph.taski.presentation.auth.signin.validation

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.signin.SignInEvent
import com.github.skytoph.taski.presentation.auth.signin.SignInEventHandler
import com.github.skytoph.taski.presentation.auth.signin.SignInState
import com.github.skytoph.taski.presentation.auth.authentication.validation.AuthValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.EmailValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.EmptinessValidator
import com.github.skytoph.taski.ui.state.StringResource

class SignInValidator : AuthValidator(
    emailValidator = EmptinessValidator(EmailValidator(null), R.string.error_email_is_invalid),
    passwordValidator = EmptinessValidator(null, R.string.error_password_is_empty),
) {

    fun validate(state: SignInState, eventHandler: SignInEventHandler) {
        val emailValidation = validateEmail(state.email.field)
        val emailError = emailValidation?.let { StringResource.ResId(it) }
        eventHandler.onEvent(SignInEvent.EmailError(emailError))

        val passwordValidation = validatePassword(state.password.field)
        val passwordError = passwordValidation?.let { StringResource.ResId(it) }
        eventHandler.onEvent(SignInEvent.PasswordError(passwordError))

        if (passwordError == null && emailError == null)
            eventHandler.onEvent(SignInEvent.Validate(true))
    }
}