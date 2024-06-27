package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper

interface EditableEntryDomainToUiMapper {
    fun map(daysAgo: Int, timesDone: Int, goal: Int, hasBorder: Boolean): EntryEditableUi

    class Base(private val now: Now) : EditableEntryDomainToUiMapper {

        override fun map(daysAgo: Int, timesDone: Int, goal: Int, hasBorder: Boolean) =
            EntryEditableUi(
//                day = now.dayOfMonths(daysAgo).toString() + "\n" + daysAgo.toString(),
                day = now.dayOfMonths(daysAgo).toString(),
                timesDone = timesDone,
                percentDone = ColorPercentMapper.toColorPercent(timesDone, goal),
                daysAgo = daysAgo,
                hasBorder = hasBorder
            )
    }
}
