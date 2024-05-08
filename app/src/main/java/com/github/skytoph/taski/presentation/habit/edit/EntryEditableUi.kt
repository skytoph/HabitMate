package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Stable
data class EditableHistoryUi(
    val entries: List<EntryEditableUi> = emptyList(),
    val month: MonthUi
) : HabitHistoryUi

@Stable
data class EntryEditableUi(
    val day: String,
    val timesDone: Int = 0,
    val color: Color,
    val daysAgo: Int
) : HabitEntryUi

@Stable
data class MonthUi(
    val timestamp: Long = 0,
    val weeks: Int,
    val alignment: TextAlign = TextAlign.Start
) : HabitEntryUi {
    fun getDisplayName(locale: Locale): String =
        SimpleDateFormat("LLL", locale).format(Date(timestamp))

    fun getDisplayYear(locale: Locale): String =
        SimpleDateFormat("YYYY", locale).format(Date(timestamp))
}