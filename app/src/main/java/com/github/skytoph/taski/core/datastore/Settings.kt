package com.github.skytoph.taski.core.datastore

import androidx.compose.runtime.Stable
import com.github.skytoph.taski.presentation.settings.theme.AppTheme
import java.util.Calendar
import java.util.TimeZone

@Stable
data class Settings(
    val weekStartsOnSunday: Initializable<Boolean> = Initializable(),
    val currentDayHighlighted: Boolean = true,
    val streaksHighlighted: Boolean = true,
    val theme: AppTheme = AppTheme.System
)

@Stable
data class Initializable<T : Any>(private val initialValue: T? = null) {
    val value: T
        get() = initialValue!!

    fun initializeIfEmpty(newValue: T): Initializable<T> = if (initialValue == null) Initializable(newValue) else this
}

class InitializeEmptyValues {
    fun initialize(value: Settings): Settings {
        val weekStartDefault = Calendar.getInstance(TimeZone.getDefault()).firstDayOfWeek == Calendar.SUNDAY

        return value.copy(
            weekStartsOnSunday = value.weekStartsOnSunday.initializeIfEmpty(weekStartDefault),
        )
    }
}