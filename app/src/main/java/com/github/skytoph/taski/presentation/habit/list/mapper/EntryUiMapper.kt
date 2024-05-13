package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.list.EntryUi

interface EntryUiMapper {
    fun map(history: EntryList, daysAgo: Int, goal: Int): EntryUi

    class Base : EntryUiMapper {
        override fun map(history: EntryList, daysAgo: Int, goal: Int): EntryUi {
            val timesDone = history.entries[daysAgo]?.timesDone ?: 0
            val colorPercent = ColorPercentMapper.toColorPercent(timesDone, goal)
            return EntryUi(daysAgo = daysAgo, percentDone = colorPercent, hasBorder = daysAgo == 0)
        }
    }
}