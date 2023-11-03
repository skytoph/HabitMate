package com.github.skytoph.taski.presentation.auth.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState

sealed class SignUpEvent {
    abstract fun handle(state: MutableState<SignUpState>)

    class TypeEmail(private val value: String) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value =
                state.value.copy(email = state.value.email.copy(field = value, errorResId = null))
        }
    }

    class TypePassword(private val value: String) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                password = state.value.password.copy(
                    field = value, errorResId = null
                )
            )
        }
    }

    class TypePasswordConfirmation(private val value: String) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                passwordConfirmation = state.value.passwordConfirmation.copy(
                    field = value, errorResId = null
                )
            )
        }
    }

    object ChangeVisibility : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
        }
    }

    class EmailError(@StringRes private val errorResId: Int?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                email = state.value.email.copy(errorResId = errorResId),
            )
        }
    }

    class PasswordError(@StringRes private val errorResId: Int?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                password = state.value.password.copy(errorResId = errorResId),
            )
        }
    }

    class PasswordConfirmationError(@StringRes private val errorResId: Int?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                passwordConfirmation = state.value.passwordConfirmation.copy(errorResId = errorResId),
            )
        }
    }

    class Error(@StringRes private val errorResId: Int?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(error = errorResId)
        }
    }

    class Validate(private val isValid: Boolean) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(isValid = isValid)
        }
    }
}
