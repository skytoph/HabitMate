package com.github.skytoph.taski.data.habit.mapper

import com.github.skytoph.taski.data.habit.database.HabitEntity
import com.github.skytoph.taski.domain.habit.Habit

fun HabitEntity.toHabit() = Habit(id, title, goal, iconName, color, emptyList())

fun Habit.toHabitDB() = HabitEntity(id, title, goal, iconName, color)