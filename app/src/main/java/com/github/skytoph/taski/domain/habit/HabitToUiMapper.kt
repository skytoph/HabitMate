package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitToUiMapper {
    fun map(title: String, goal: Int, icon: String, color: Long, history: List<Long>): HabitUi

}