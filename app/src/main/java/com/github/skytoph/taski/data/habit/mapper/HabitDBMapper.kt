package com.github.skytoph.taski.data.habit.mapper

import com.github.skytoph.taski.data.habit.database.HabitDB
import com.github.skytoph.taski.domain.habit.Habit

fun HabitDB.toHabit() = Habit(id, title, goal, iconName, color, emptyList())

fun Habit.toHabitDB() = HabitDB(id, title, goal, iconName, color)