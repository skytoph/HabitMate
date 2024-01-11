package com.github.skytoph.taski.presentation.core.state

data class FieldState(
    val field: String = "",
    val error: StringResource? = null,
)