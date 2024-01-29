package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.ui.text.style.TextAlign
import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EditableHistoryUi(
    val entries: List<EntryEditableUi> = emptyList(),
    val months: List<MonthUi> = emptyList()
) : HabitHistoryUi

data class EntryEditableUi(
    val day: String,
    val timesDone: Int = 0,
    val daysAgo: Int
) : HabitEntryUi {

    fun donePercent(goal: Int): Int = ColorPercentMapper.percentDone(timesDone, goal)

    fun colorPercent(goal: Int): Float = ColorPercentMapper.toColorPercent(timesDone, goal)
}

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