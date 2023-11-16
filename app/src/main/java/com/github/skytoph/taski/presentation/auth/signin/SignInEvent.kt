package com.github.skytoph.taski.presentation.auth.signin

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.AuthState
import com.github.skytoph.taski.ui.state.StringResource

sealed class SignInEvent {
    abstract fun handle(state: MutableState<SignInState>)

    class TypeEmail(private val value: String) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value =
                state.value.copy(email = state.value.email.copy(field = value, error = null))
        }
    }

    class TypePassword(private val value: String) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(
                password = state.value.password.copy(field = value, error = null)
            )
        }
    }

    object ChangeVisibility : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
        }
    }

    class EmailError(private val error: StringResource?) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(
                email = state.value.email.copy(error = error), isValid = false
            )
        }
    }

    class PasswordError(private val error: StringResource?) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(
                password = state.value.password.copy(error = error), isValid = false
            )
        }
    }

    class Error(private val error: StringResource?) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(error = error, isValid = false)
        }
    }

    class Validate(private val isValid: Boolean) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(isValid = isValid)
        }
    }

    class Auth(private val authResult: AuthResult) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value =
                state.value.copy(auth = AuthState(authResult.user != null, authResult.error))
        }
    }
}
