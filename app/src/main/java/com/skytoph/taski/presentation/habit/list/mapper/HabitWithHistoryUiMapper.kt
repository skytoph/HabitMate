package com.skytoph.taski.presentation.habit.list.mapper

import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.habit.HabitHistoryUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper

interface HabitWithHistoryUiMapper<T : HabitHistoryUi, V : ViewType> {
    fun map(
        habitWithEntries: HabitWithEntries,
        entries: Int,
        isBorderOn: Boolean,
        isFirstDaySunday: Boolean
    ): HabitWithHistoryUi<T>

    abstract class Abstract<T : HabitHistoryUi, V : ViewType>(
        private val habitMapper: HabitUiMapper,
        private val historyMapper: HabitHistoryUiMapper<T, V>,
        private val statsMapper: HabitStatisticsMapper
    ) : HabitWithHistoryUiMapper<T, V> {

        override fun map(
            habitWithEntries: HabitWithEntries,
            entries: Int,
            isBorderOn: Boolean,
            isFirstDaySunday: Boolean
        ): HabitWithHistoryUi<T> {
            val habitUi = habitMapper.map(habitWithEntries.habit)
            val historyUi = historyMapper.map(
                page = entries,
                goal = habitWithEntries.habit.goal,
                history = habitWithEntries.entries,
                stats = HabitStatisticsUi(),
                isBorderOn = isBorderOn,
                isFirstDaySunday = isFirstDaySunday
            )
            return HabitWithHistoryUi(habit = habitUi, history = historyUi)
        }
    }
}