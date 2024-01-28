package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.ui.text.style.TextAlign
import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EditableHistoryUi(
    val entries: List<EntryEditableUi> = emptyList(),
    val months: List<MonthUi> = emptyList()
) : HabitHistoryUi

data class EntryEditableUi(
    val day: String,
    val colorPercent: Float = 0F,
    val daysAgo: Int
) : HabitEntryUi

data class MonthUi(
    val timestamp: Long = 0,
    val weeks: Int,
    val alignment: TextAlign = TextAlign.Start
) {
    fun getDisplayName(locale: Locale): String =
        SimpleDateFormat("LLL", locale).format(Date(timestamp))

    fun getDisplayYear(locale: Locale): String =
        SimpleDateFormat("YYYY", locale).format(Date(timestamp))
}