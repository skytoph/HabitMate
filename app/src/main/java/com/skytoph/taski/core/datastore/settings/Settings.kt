package com.skytoph.taski.core.datastore.settings

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Stable
import java.util.Calendar

@Stable
data class Settings(
    val weekStartsOnSunday: Initializable<Boolean> = Initializable(default = true),
    val currentDayHighlighted: Boolean = true,
    val time24hoursFormat: Initializable<Boolean> = Initializable(default = true),
    val streaksHighlighted: Boolean = true,
    val showIconWarning: Boolean = true,
    val allowCrashlytics: Boolean? = null,
    val sortIcons: Boolean = false,
    val theme: AppTheme? = null,
    val view: HabitsView = HabitsView(),
    val habitHistoryView: HabitHistoryView = HabitHistoryView.Calendar,
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

class InitializeEmptyValues(private val context: Context? = null) {
    fun initialize(value: Settings): Settings {
        val weekStartDefault = Calendar.getInstance().firstDayOfWeek == Calendar.SUNDAY
        val time24hoursDefault = if (context == null) true else DateFormat.is24HourFormat(context)

        return value.copy(
            weekStartsOnSunday = value.weekStartsOnSunday.initializeIfEmpty(weekStartDefault),
            time24hoursFormat = value.time24hoursFormat.initializeIfEmpty(time24hoursDefault),
            theme = value.theme ?: AppTheme.System,
            view = HabitsView(initialized = true)
        )
    }
}