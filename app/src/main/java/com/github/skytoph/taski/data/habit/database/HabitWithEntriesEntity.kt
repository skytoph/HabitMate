package com.github.skytoph.taski.data.habit.database

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.github.skytoph.taski.data.habit.mapper.HabitDBToDomainMapper

@Keep
data class HabitWithEntriesEntity(

    @Embedded
    val habit: HabitEntity,

    @Relation(parentColumn = "id", entityColumn = "habit_id")
    val entries: List<EntryEntity>
) {

    fun map(mapper: HabitDBToDomainMapper) = mapper.map(habit, entries)
}