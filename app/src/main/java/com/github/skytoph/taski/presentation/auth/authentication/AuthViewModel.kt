package com.github.skytoph.taski.presentation.auth.authentication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val state = MutableStateFlow(AuthState())

    fun onAuthResult(result: AuthResult) {
        state.update {
            it.copy(
                isSignInSuccessful = result.user != null,
                signInError = result.error
            )
        }
    }

    fun state() = state.asStateFlow()

    fun resetState() {
        state.update { AuthState() }
    }

}