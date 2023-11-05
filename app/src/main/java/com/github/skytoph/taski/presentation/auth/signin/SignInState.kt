package com.github.skytoph.taski.presentation.auth.signin

import com.github.skytoph.taski.ui.state.StringResource
import com.github.skytoph.taski.ui.state.FieldState

data class SignInState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val isPasswordVisible: Boolean = false,
    val error: StringResource? = null,
    val isValid: Boolean = false
)