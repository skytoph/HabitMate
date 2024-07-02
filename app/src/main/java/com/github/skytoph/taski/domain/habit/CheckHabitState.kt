package com.github.skytoph.taski.domain.habit

interface CheckHabitState {
    suspend fun isHabitDone(habitId: Long): Boolean
}
