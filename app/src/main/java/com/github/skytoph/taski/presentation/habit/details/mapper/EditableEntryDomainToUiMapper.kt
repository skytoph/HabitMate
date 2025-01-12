package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi

interface EditableEntryDomainToUiMapper {
    fun map(daysAgo: Int, timesDone: Int, goal: Int, hasBorder: Boolean, isDisabled: Boolean = false): EntryEditableUi

    class Base(private val now: Now) : EditableEntryDomainToUiMapper {

        override fun map(daysAgo: Int, timesDone: Int, goal: Int, hasBorder: Boolean, isDisabled: Boolean) =
            EntryEditableUi(
                day = now.dayOfMonths(daysAgo).toString(),
                timesDone = timesDone,
                daysAgo = daysAgo,
                hasBorder = hasBorder,
                isDisabled = isDisabled
            )
    }
}
