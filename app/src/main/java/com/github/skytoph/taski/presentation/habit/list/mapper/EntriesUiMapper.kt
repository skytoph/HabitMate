package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.core.color.habitColor
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class EntriesUiMapper(
    private val now: Now,
    private val numberOfCells: Int = COLUMNS * ROWS
) : HabitHistoryUiMapper<HistoryUi> {

    override fun map(
        column: Int, goal: Int, history: EntryList, habitColor: Color, defaultColor: Color
    ): HistoryUi {
        val todayPosition = 7 - now.dayOfWeek()
        val entries = (numberOfCells - todayPosition - 1 downTo -todayPosition).map { index ->
            val timesDone = history.entries[index]?.timesDone ?: 0
            val colorPercent = ColorPercentMapper.toColorPercent(timesDone, goal)
            val color = habitColor(colorPercent, defaultColor, habitColor)
            EntryUi(color = color, hasBorder = index == todayPosition)
        }
        val todayDonePercent =
            ColorPercentMapper.toColorPercent(history.entries[todayPosition]?.timesDone ?: 0, goal)
        return HistoryUi(entries = entries, todayDonePercent = todayDonePercent)
    }

    private fun todayPosition(): Int = numberOfCells - 8 + now.dayOfWeek() // todo remove

    private companion object {
        const val COLUMNS = 50
        const val ROWS = 7
    }
}