package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi

interface EditableEntryDomainToUiMapper {
    fun map(daysAgo: Int, timesDone: Int): EntryEditableUi

    class Base(private val now: Now) : EditableEntryDomainToUiMapper {

        override fun map(daysAgo: Int, timesDone: Int) = EntryEditableUi(
            day = now.dayOfMonths(daysAgo).toString(),
            timesDone = timesDone,
            daysAgo = daysAgo
        )
    }
}
