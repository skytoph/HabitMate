package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi

interface HabitWithHistoryUiMapper<T : HabitHistoryUi> {
    fun map(habit: Habit, history: EntryList): HabitWithHistoryUi<T>

    abstract class Abstract<T : HabitHistoryUi>(
        private val habitMapper: HabitUiMapper,
        private val historyMapper: HabitHistoryUiMapper<T>
    ) : HabitWithHistoryUiMapper<T> {

        override fun map(habit: Habit, history: EntryList): HabitWithHistoryUi<T> =
            HabitWithHistoryUi(
                habit = habitMapper.map(habit),
                history = historyMapper.map(goal = habit.goal, history = history),
            )
    }
}