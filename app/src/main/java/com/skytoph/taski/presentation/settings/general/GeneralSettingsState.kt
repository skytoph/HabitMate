package com.skytoph.taski.presentation.settings.general

data class GeneralSettingsState(
    val weekStartsOnSunday: Boolean = true,
    val currentDayHighlighted: Boolean = true,
    val streaksHighlighted: Boolean = true,
)
