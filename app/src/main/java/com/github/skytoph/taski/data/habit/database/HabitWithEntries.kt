package com.github.skytoph.taski.data.habit.database

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithEntries(

    @Embedded
    val habit: HabitEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val entries: List<EntryEntity>
)