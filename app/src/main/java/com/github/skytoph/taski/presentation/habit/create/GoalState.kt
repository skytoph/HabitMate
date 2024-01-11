package com.github.skytoph.taski.presentation.habit.create

import com.github.skytoph.taski.presentation.habit.HabitUi

data class GoalState(
    val value: Int = HabitUi.MIN_GOAL,
    val canBeIncreased: Boolean = true,
    val canBeDecreased: Boolean = false
)