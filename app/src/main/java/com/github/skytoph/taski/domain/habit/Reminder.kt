package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.data.habit.database.ReminderEntity
import com.github.skytoph.taski.presentation.habit.ReminderUi

sealed class Reminder {
    abstract fun mapToDB(): ReminderEntity?
    abstract fun mapToUi(): ReminderUi

    data object None : Reminder() {
        override fun mapToDB(): ReminderEntity? = null
        override fun mapToUi(): ReminderUi = ReminderUi()
    }

    class SwitchedOn(val hour: Int, val minute: Int) : Reminder() {
        override fun mapToDB(): ReminderEntity = ReminderEntity(hour = hour, minute = minute)
        override fun mapToUi(): ReminderUi = ReminderUi(switchedOn = true, hour = hour, minute = minute)
    }
}