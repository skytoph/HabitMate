package com.github.skytoph.taski.data.habit.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Keep
@Entity(
    tableName = "entry",
    primaryKeys = ["habit_id", "timestamp"],
    foreignKeys = [ForeignKey(
        entity = HabitEntity::class,
        parentColumns = ["id"],
        childColumns = ["habit_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )]
)
data class EntryEntity(

    @ColumnInfo(name = "habit_id")
    val habitId: Long,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "times_done")
    val timesDone: Int
)