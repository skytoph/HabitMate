package com.skytoph.taski.data.habit.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo

@Keep
data class ReminderEntity(
    @ColumnInfo(name = "reminder_hour")
    val hour: Int,

    @ColumnInfo(name = "reminder_minute")
    val minute: Int
)