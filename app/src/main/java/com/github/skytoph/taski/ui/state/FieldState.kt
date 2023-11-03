package com.github.skytoph.taski.ui.state

import androidx.annotation.StringRes

data class FieldState(
    val field: String = "",
    @StringRes val errorResId: Int? = null,
)