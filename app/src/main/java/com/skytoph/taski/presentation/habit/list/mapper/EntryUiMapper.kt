package com.skytoph.taski.presentation.habit.list.mapper

import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.presentation.habit.list.EntryUi

interface EntryUiMapper {
    fun map(entry: Entry?, daysAgo: Int, goal: Int, isBorderOn: Boolean): EntryUi

    class Base : EntryUiMapper {
        override fun map(
            entry: Entry?, daysAgo: Int, goal: Int, isBorderOn: Boolean
        ): EntryUi {
            val timesDone = entry?.timesDone ?: 0
            val colorPercent = ColorPercentMapper.toColorPercent(timesDone, goal)
            return EntryUi(
                daysAgo = daysAgo,
                percentDone = colorPercent,
                timesDone = timesDone,
                hasBorder = isBorderOn && daysAgo == 0
            )
        }
    }
}