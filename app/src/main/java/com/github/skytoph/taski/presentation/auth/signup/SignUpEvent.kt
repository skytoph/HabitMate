package com.github.skytoph.taski.presentation.auth.signup

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.AuthState
import com.github.skytoph.taski.presentation.core.state.StringResource

sealed class SignUpEvent {
    abstract fun handle(state: MutableState<SignUpState>)

    class TypeEmail(private val value: String) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value =
                state.value.copy(email = state.value.email.copy(field = value, error = null))
        }
    }

    class TypePassword(private val value: String) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                password = state.value.password.copy(field = value, error = null)
            )
        }
    }

    class TypePasswordConfirmation(private val value: String) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                passwordConfirmation = state.value.passwordConfirmation.copy(
                    field = value, error = null
                )
            )
        }
    }

    object ChangeVisibility : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
        }
    }

    class EmailError(private val error: StringResource?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                email = state.value.email.copy(error = error),
            )
        }
    }

    class PasswordError(private val error: StringResource?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                password = state.value.password.copy(error = error),
            )
        }
    }

    class PasswordConfirmationError(private val error: StringResource?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(
                passwordConfirmation = state.value.passwordConfirmation.copy(error = error),
            )
        }
    }

    class Error(private val error: StringResource?) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(error = error)
        }
    }

    class Validate(private val isValid: Boolean) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value = state.value.copy(isValid = isValid)
        }
    }

    class Auth(private val authResult: AuthResult) : SignUpEvent() {
        override fun handle(state: MutableState<SignUpState>) {
            state.value =
                state.value.copy(auth = AuthState(authResult.user != null, authResult.error))
        }
    }
}
