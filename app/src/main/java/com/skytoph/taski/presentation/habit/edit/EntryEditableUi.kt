package com.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.style.TextAlign
import com.skytoph.taski.presentation.habit.HabitEntryUi
import com.skytoph.taski.presentation.habit.HabitHistoryUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Stable
data class EditableHistoryUi(
    val entries: Map<Int, EntryEditableUi> = emptyMap(),
    val month: MonthUi
) : HabitHistoryUi {
    val entriesList: List<EntryEditableUi>
        get() = entries.values.toList()
}

@Stable
data class EntryEditableUi(
    val day: String,
    val timesDone: Int = 0,
    val daysAgo: Int = 0,
    val streakType: StreakType? = null,
    val isDisabled: Boolean = false,
) : HabitEntryUi

enum class StreakType {
    Start,
    End,
    Middle,
    Dot
}

@Stable
data class MonthUi(
    val timestamp: Long = 0,
    val weeks: Int,
    val index: Int,
    val alignment: TextAlign = TextAlign.Start
) : HabitEntryUi {

    fun getDisplayName(locale: Locale): String =
        SimpleDateFormat("LLLL yyyy", locale).format(Date(timestamp))

    fun getDisplayMonth(locale: Locale): String =
        SimpleDateFormat("LLL", locale).format(Date(timestamp))

    fun getDisplayYear(locale: Locale): String =
        SimpleDateFormat("YYYY", locale).format(Date(timestamp))
}