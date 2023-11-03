package com.github.skytoph.taski.presentation.auth.signup.validation

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.signup.SignUpEvent
import com.github.skytoph.taski.presentation.auth.signup.SignUpEventHandler
import com.github.skytoph.taski.presentation.auth.signup.SignUpState
import com.github.skytoph.taski.presentation.auth.validation.AuthValidator
import com.github.skytoph.taski.presentation.auth.validation.EmailValidator
import com.github.skytoph.taski.presentation.auth.validation.EmptinessValidator
import com.github.skytoph.taski.presentation.auth.validation.MinLengthValidator
import com.github.skytoph.taski.presentation.auth.validation.NumericCharValidator

class SignUpValidator : AuthValidator(
    passwordValidator =
    EmptinessValidator(
        MinLengthValidator(NumericCharValidator(null)), R.string.error_password_is_empty
    ),
    emailValidator =
    EmptinessValidator(EmailValidator(null), R.string.error_email_is_invalid)
) {

    fun validate(state: SignUpState, eventHandler: SignUpEventHandler) {
        val emailErrorResId = validateEmail(state.email.field)
        eventHandler.onEvent(SignUpEvent.EmailError(emailErrorResId))

        val passwordErrorResId = validatePassword(state.password.field)
        eventHandler.onEvent(SignUpEvent.PasswordError(passwordErrorResId))

        val passwordConfirmErrorResId =
            confirmPassword(state.password.field, state.passwordConfirmation.field)
        eventHandler.onEvent(SignUpEvent.PasswordConfirmationError(passwordConfirmErrorResId))

        if (passwordErrorResId == null && emailErrorResId == null && passwordConfirmErrorResId == null)
            eventHandler.onEvent(SignUpEvent.Validate(true))
    }

    private fun confirmPassword(password: String, passwordConfirmation: String): Int? =
        when {
            passwordConfirmation.isEmpty() -> R.string.error_password_confirmation
            password != passwordConfirmation -> R.string.error_password_mismatch
            else -> null
        }
}