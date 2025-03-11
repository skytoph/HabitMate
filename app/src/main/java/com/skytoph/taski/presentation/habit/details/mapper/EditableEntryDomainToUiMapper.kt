package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.core.Now
import com.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.skytoph.taski.presentation.habit.edit.StreakType

interface EditableEntryDomainToUiMapper {
    fun map(daysAgo: Int, timesDone: Int, goal: Int, streakType: StreakType?, isDisabled: Boolean = false)
            : EntryEditableUi

    class Base(private val now: Now) : EditableEntryDomainToUiMapper {

        override fun map(daysAgo: Int, timesDone: Int, goal: Int, streakType: StreakType?, isDisabled: Boolean) =
            EntryEditableUi(
                day = now.dayOfMonths(daysAgo).toString(),
                timesDone = timesDone,
                daysAgo = daysAgo,
                streakType = streakType,
                isDisabled = isDisabled
            )
    }
}
