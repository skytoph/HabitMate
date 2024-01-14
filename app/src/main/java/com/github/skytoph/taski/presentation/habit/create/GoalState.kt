package com.github.skytoph.taski.presentation.habit.create

import com.github.skytoph.taski.presentation.habit.HabitUi

data class GoalState(
    val value: Int = HabitUi.MIN_GOAL,
    val canBeIncreased: Boolean = value < HabitUi.MAX_GOAL,
    val canBeDecreased: Boolean = value > HabitUi.MIN_GOAL
)