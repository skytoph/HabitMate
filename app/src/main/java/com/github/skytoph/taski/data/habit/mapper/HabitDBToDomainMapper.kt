package com.github.skytoph.taski.data.habit.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.data.habit.database.HabitEntity
import com.github.skytoph.taski.domain.habit.Habit

interface HabitDBToDomainMapper {
    fun map(habit: HabitEntity, entries: List<EntryEntity>): Habit

    class Base(private val now: Now) : HabitDBToDomainMapper {

        override fun map(habit: HabitEntity, entries: List<EntryEntity>): Habit =
            Habit(
                habit.id,
                habit.title,
                habit.goal,
                habit.iconName,
                habit.color,
                entries.associateBy({ now.daysAgo(it.timestamp) }, { it.toEntry() })
            )
    }
}