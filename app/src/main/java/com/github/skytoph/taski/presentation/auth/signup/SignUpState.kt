package com.github.skytoph.taski.presentation.auth.signup

import com.github.skytoph.taski.ui.state.StringResource
import com.github.skytoph.taski.ui.state.FieldState

data class SignUpState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val passwordConfirmation: FieldState = FieldState(),
    val error: StringResource? = null,
    val isPasswordVisible: Boolean = false,
    val isValid: Boolean = false
)