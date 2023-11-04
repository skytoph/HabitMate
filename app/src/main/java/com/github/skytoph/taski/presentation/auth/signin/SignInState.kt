package com.github.skytoph.taski.presentation.auth.signin

import androidx.annotation.StringRes
import com.github.skytoph.taski.ui.state.FieldState

data class SignInState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val isPasswordVisible: Boolean = false,
    @StringRes val error: Int? = null,
    val isValid: Boolean = false
)