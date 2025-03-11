package com.skytoph.taski.data.habit.mapper

import com.skytoph.taski.data.habit.database.EntryEntity
import com.skytoph.taski.data.habit.database.HabitEntity
import com.skytoph.taski.data.habit.database.ReminderEntity
import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.domain.habit.Habit
import com.skytoph.taski.domain.habit.Reminder

fun EntryEntity.toEntry() = Entry(timestamp, timesDone)

fun Habit.toHabitDB() = HabitEntity(
    id, title, description, goal, iconName, color, priority, isArchived, frequency.mapToDB(), reminder.mapToDB()
)

fun ReminderEntity?.map() = if (this != null) Reminder.SwitchedOn(hour, minute) else Reminder.None

fun HabitEntity.toHabit() =
    Habit(id, title, description, goal, iconName, color, priority, isArchived, frequency.map(), reminder.map())