package com.skytoph.taski.data.habit.mapper

import com.skytoph.taski.data.habit.database.EntryEntity
import com.skytoph.taski.data.habit.database.HabitEntity
import com.skytoph.taski.domain.habit.HabitWithEntries

interface HabitDBToDomainMapper {
    fun map(habit: HabitEntity, entries: List<EntryEntity>): HabitWithEntries

    class Base(private val entryMapper: EntryListMapper) :
        HabitDBToDomainMapper {

        override fun map(habit: HabitEntity, entries: List<EntryEntity>): HabitWithEntries =
            HabitWithEntries(habit = habit.toHabit(), entries = entryMapper.map(entries))
    }
}