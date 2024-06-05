package com.github.skytoph.taski.data.habit.mapper

import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.data.habit.database.HabitEntity
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.Habit

fun EntryEntity.toEntry() = Entry(timestamp, timesDone)

fun Habit.toHabitDB() =
    HabitEntity(id, title, goal, iconName, color, priority, isArchived, frequency.mapToDB())

fun HabitEntity.toHabit() = Habit(id, title, goal, iconName, color, priority, isArchived, frequency.map())