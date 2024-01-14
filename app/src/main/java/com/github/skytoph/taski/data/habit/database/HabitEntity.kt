package com.github.skytoph.taski.data.habit.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class HabitEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "goal")
    val goal: Int,

    @ColumnInfo(name = "icon_name")
    val iconName: String,

    @ColumnInfo(name = "color")
    val color: Int,
)