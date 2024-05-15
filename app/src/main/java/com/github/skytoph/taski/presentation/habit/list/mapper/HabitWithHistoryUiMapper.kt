package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType

interface HabitWithHistoryUiMapper<T : HabitHistoryUi, V : ViewType> {
    fun map(habit: Habit, history: EntryList, entries: Int): HabitWithHistoryUi<T>

    abstract class Abstract<T : HabitHistoryUi, V : ViewType>(
        private val habitMapper: HabitUiMapper,
        private val historyMapper: HabitHistoryUiMapper<T, V>
    ) : HabitWithHistoryUiMapper<T, V> {

        override fun map(habit: Habit, history: EntryList, entries: Int): HabitWithHistoryUi<T> {
            val habitUi = habitMapper.map(habit)
            val historyUi = historyMapper.map(page = entries, goal = habit.goal, history = history)
            return HabitWithHistoryUi(habit = habitUi, history = historyUi)
        }
    }
}