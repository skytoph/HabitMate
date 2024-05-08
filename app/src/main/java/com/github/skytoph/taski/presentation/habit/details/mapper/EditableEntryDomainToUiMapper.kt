package com.github.skytoph.taski.presentation.habit.details.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.core.color.habitColor
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper

interface EditableEntryDomainToUiMapper {
    fun map(
        daysAgo: Int, timesDone: Int, goal: Int, habitColor: Color, defaultColor: Color
    ): EntryEditableUi

    class Base(private val now: Now) : EditableEntryDomainToUiMapper {

        override fun map(
            daysAgo: Int, timesDone: Int, goal: Int, habitColor: Color, defaultColor: Color
        ) = EntryEditableUi(
            day = now.dayOfMonths(daysAgo).toString(),
            timesDone = timesDone,
            color = habitColor(
                ColorPercentMapper.toColorPercent(timesDone, goal),
                defaultColor,
                habitColor
            ),
            daysAgo = daysAgo
        )
    }
}
