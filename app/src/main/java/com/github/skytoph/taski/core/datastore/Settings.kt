package com.github.skytoph.taski.core.datastore

import androidx.compose.runtime.Stable
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView
import com.github.skytoph.taski.presentation.settings.theme.AppTheme
import java.util.Calendar

@Stable
data class Settings(
    val weekStartsOnSunday: Initializable<Boolean> = Initializable(default = true),
    val currentDayHighlighted: Boolean = true,
    val streaksHighlighted: Boolean = true,
    val theme: AppTheme = AppTheme.Dark,
    val view: HabitsView = HabitsView(),
    val lastBackupSaved: Long? = null
) {
    companion object {
        fun default(mapper: InitializeEmptyValues): Settings = mapper.initialize(Settings())
        val notInitialized: Settings = Settings()
    }
}

@Stable
data class Initializable<T : Any>(private val initialValue: T? = null, private val default: T) {
    val value: T
        get() = initialValue ?: default

    fun initializeIfEmpty(newValue: T): Initializable<T> =
        if (initialValue == null) Initializable(newValue, newValue) else this
}

class InitializeEmptyValues {
    fun initialize(value: Settings): Settings {
        val weekStartDefault = Calendar.getInstance().firstDayOfWeek == Calendar.SUNDAY

        return value.copy(
            weekStartsOnSunday = value.weekStartsOnSunday.initializeIfEmpty(weekStartDefault),
            view = HabitsView(initialized = true)
        )
    }
}