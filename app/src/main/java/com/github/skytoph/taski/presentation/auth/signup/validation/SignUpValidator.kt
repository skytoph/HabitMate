package com.github.skytoph.taski.presentation.auth.signup.validation

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.validation.AuthValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.Validator
import com.github.skytoph.taski.presentation.auth.signup.SignUpEvent
import com.github.skytoph.taski.presentation.auth.signup.SignUpEventHandler
import com.github.skytoph.taski.presentation.auth.signup.SignUpState
import com.github.skytoph.taski.presentation.core.state.StringResource

class SignUpValidator(emailValidator: Validator, passwordValidator: Validator) :
    AuthValidator(emailValidator = emailValidator, passwordValidator = passwordValidator) {

    fun validate(state: SignUpState, eventHandler: SignUpEventHandler) {
        val emailValidation = validateEmail(state.email.field)
        val emailError = emailValidation?.let { StringResource.ResId(it) }
        eventHandler.onEvent(SignUpEvent.EmailError(emailError))

        val passwordValidation = validatePassword(state.password.field)
        val passwordError = passwordValidation?.let { StringResource.ResId(it) }
        eventHandler.onEvent(SignUpEvent.PasswordError(passwordError))

        val confirmationValidation =
            confirmPassword(state.password.field, state.passwordConfirmation.field)
        val confirmationError = confirmationValidation?.let { StringResource.ResId(it) }
        eventHandler.onEvent(SignUpEvent.PasswordConfirmationError(confirmationError))

        if (passwordValidation == null && emailValidation == null && confirmationValidation == null)
            eventHandler.onEvent(SignUpEvent.Validate(true))
    }

    private fun confirmPassword(password: String, passwordConfirmation: String): Int? =
        when {
            passwordConfirmation.isEmpty() -> R.string.error_password_confirmation
            password != passwordConfirmation -> R.string.error_password_mismatch
            else -> null
        }
}