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
import com.github.skytoph.taski.ui.state.StringResource

class SignUpValidator : AuthValidator(
    emailValidator =
    EmptinessValidator(EmailValidator(null), R.string.error_email_is_invalid),
    passwordValidator =
    EmptinessValidator(
        MinLengthValidator(NumericCharValidator(null)), R.string.error_password_is_empty
    )
) {

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