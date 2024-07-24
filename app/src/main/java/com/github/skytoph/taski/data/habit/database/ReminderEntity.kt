package com.github.skytoph.taski.data.habit.database

import androidx.room.ColumnInfo

data class ReminderEntity(
    @ColumnInfo(name = "reminder_hour")
    val hour: Int,

    @ColumnInfo(name = "reminder_minute")
    val minute: Int
)