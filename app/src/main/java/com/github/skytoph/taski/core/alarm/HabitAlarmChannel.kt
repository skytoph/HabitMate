package com.github.skytoph.taski.core.alarm

interface HabitAlarmChannel {
    val name: String
    val id: String

    object Base : HabitAlarmChannel {
        override val name: String = "notifications"
        override val id = "habit_alarm_id"
    }
}