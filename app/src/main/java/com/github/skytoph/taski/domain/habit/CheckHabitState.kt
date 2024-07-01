package com.github.skytoph.taski.domain.habit

interface CheckHabitState {
    fun isHabitDone(habitId: Long): Boolean
}
