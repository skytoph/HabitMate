package com.skytoph.taski.domain.habit

interface CheckHabitState {
    suspend fun notCompleted(habitId: Long, isFirstDaySunday: Boolean = false): Boolean
    suspend fun habit(id: Long): Habit
}
