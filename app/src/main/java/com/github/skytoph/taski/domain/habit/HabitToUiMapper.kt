package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitToUiMapper {
    fun map(
        id: Long, title: String, goal: Int, icon: String, color: Int, history: List<Entry>
    ): HabitUi
}