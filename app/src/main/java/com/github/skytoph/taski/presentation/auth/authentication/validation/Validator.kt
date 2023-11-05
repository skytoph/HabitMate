package com.github.skytoph.taski.presentation.auth.authentication.validation

import androidx.annotation.StringRes
import com.github.skytoph.taski.R

interface Validator {
    fun validate(value: String): ValidationResult
    fun isValid(value: String): Boolean

    abstract class Abstract(@StringRes val errorResId: Int?, private val validator: Validator?) :
        Validator {

        override fun validate(value: String): ValidationResult = when {
            !isValid(value) -> ValidationResult(errorResId)
            validator == null -> ValidationResult(null)
            else -> validator.validate(value)
        }
    }
}

data class ValidationResult(@StringRes val errorResId: Int?)

class MinLengthValidator(validator: Validator?) :
    Validator.Abstract(R.string.error_password_is_short, validator) {

    override fun isValid(value: String): Boolean =
        value.length >= MIN_LENGTH

    companion object {
        private const val MIN_LENGTH = 6
    }
}

class NumericCharValidator(validator: Validator?) :
    Validator.Abstract(R.string.error_password_should_contain_numeric_character, validator) {

    override fun isValid(value: String): Boolean =
        value.contains("[0-9]".toRegex())
}

class EmptinessValidator(validator: Validator?, errorResId: Int?) :
    Validator.Abstract(errorResId, validator) {

    override fun isValid(value: String): Boolean = value.isNotEmpty()
}

class EmailValidator(validator: Validator?) :
    Validator.Abstract(R.string.error_email_is_invalid, validator) {

    override fun isValid(value: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches();
}

abstract class AuthValidator(
    private val passwordValidator: Validator,
    private val emailValidator: Validator
) {

    fun validatePassword(password: String): Int? = passwordValidator.validate(password).errorResId

    fun validateEmail(email: String): Int? = emailValidator.validate(email).errorResId
}