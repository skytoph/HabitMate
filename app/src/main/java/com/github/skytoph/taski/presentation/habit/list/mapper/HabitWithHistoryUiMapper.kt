package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi

interface HabitWithHistoryUiMapper<T : HabitHistoryUi> {
    fun map(habit: Habit, history: EntryList, defaultColor: Color): HabitWithHistoryUi<T>

    abstract class Abstract<T : HabitHistoryUi>(
        private val habitMapper: HabitUiMapper,
        private val historyMapper: HabitHistoryUiMapper<T>
    ) : HabitWithHistoryUiMapper<T> {

        override fun map(
            habit: Habit, history: EntryList, defaultColor: Color
        ): HabitWithHistoryUi<T> {
            val habitUi = habitMapper.map(habit)
            val historyUi = historyMapper.map(
                goal = habit.goal,
                history = history,
                habitColor = habitUi.color,
                defaultColor = defaultColor
            )
            return HabitWithHistoryUi(habit = habitUi, history = historyUi)
        }
    }
}