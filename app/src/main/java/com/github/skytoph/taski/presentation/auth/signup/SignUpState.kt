package com.github.skytoph.taski.presentation.auth.signup

import androidx.annotation.StringRes
import com.github.skytoph.taski.ui.state.FieldState

data class SignUpState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val passwordConfirmation: FieldState = FieldState(),
    @StringRes val error: Int? = null,
    val isPasswordVisible: Boolean = false,
    val isValid: Boolean = false
)