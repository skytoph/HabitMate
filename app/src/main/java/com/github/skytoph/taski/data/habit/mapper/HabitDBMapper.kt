package com.github.skytoph.taski.data.habit.mapper

import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.data.habit.database.HabitEntity
import com.github.skytoph.taski.data.habit.database.HabitWithEntries
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.Habit

fun HabitEntity.toHabit() = Habit(id, title, goal, iconName, color, emptyList())

fun HabitWithEntries.toHabit() = Habit(
    habit.id,
    habit.title,
    habit.goal,
    habit.iconName,
    habit.color,
    entries.map { it.toEntry() })

fun EntryEntity.toEntry() = Entry(timestamp, timesDone)

fun Habit.toHabitDB() = HabitEntity(id, title, goal, iconName, color)