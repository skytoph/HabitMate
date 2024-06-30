package com.github.skytoph.taski.domain.habit

interface IsHabitDone {
    fun isHabitDone(habitId: Long): Boolean
}
