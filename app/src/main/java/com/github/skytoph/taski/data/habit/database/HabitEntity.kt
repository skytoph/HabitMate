package com.github.skytoph.taski.data.habit.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "habit")
data class HabitEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "goal")
    val goal: Int,

    @ColumnInfo(name = "icon_name")
    val iconName: String,

    @ColumnInfo(name = "color")
    val color: Int,

    @ColumnInfo(name = "priority")
    val priority: Int,

    @ColumnInfo(name = "archived")
    val isArchived: Boolean,

    @ColumnInfo(name = "frequency")
    val frequency: FrequencyEntity,

    @Embedded
    val reminder: ReminderEntity? = null,
)