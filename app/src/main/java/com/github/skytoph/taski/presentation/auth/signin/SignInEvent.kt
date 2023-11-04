package com.github.skytoph.taski.presentation.auth.signin

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState

sealed class SignInEvent {
    abstract fun handle(state: MutableState<SignInState>)

    class TypeEmail(private val value: String) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value =
                state.value.copy(email = state.value.email.copy(field = value, errorResId = null))
        }
    }

    class TypePassword(private val value: String) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(password = state.value.password.copy(field = value, errorResId = null))
        }
    }

    object ChangeVisibility : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
        }
    }

    class EmailError(@StringRes private val errorResId: Int?) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(
                email = state.value.email.copy(errorResId = errorResId),
            )
        }
    }

    class PasswordError(@StringRes private val errorResId: Int?) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value =
                state.value.copy(
                    password = state.value.password.copy(errorResId = errorResId),
                )
        }
    }

    class Error(@StringRes private val errorResId: Int?) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(error = errorResId)
        }
    }

    class Validate(private val isValid: Boolean) : SignInEvent() {
        override fun handle(state: MutableState<SignInState>) {
            state.value = state.value.copy(isValid = isValid)
        }
    }
}
