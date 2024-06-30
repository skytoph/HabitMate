package com.github.skytoph.taski.presentation.habit.edit.mapper

import android.app.AlarmManager
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.presentation.habit.HabitUi
import java.util.Calendar

interface HabitNotificationMapper {
    fun map(habit: HabitUi): List<AlarmItem>

    class Base(private val dateMapper: HabitDateMapper) :
        HabitNotificationMapper {
        override fun map(habit: HabitUi): List<AlarmItem> =
            habit.frequency.dates(dateMapper).map { calendar ->
                AlarmItem(
                    habitId = habit.id,
                    title = habit.title,
                    message = R.string.habit_reminder_message,
                    icon = habit.icon,
                    calendar = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
                        set(Calendar.MINUTE, habit.reminder.minute)
                    },
                    interval = AlarmManager.INTERVAL_DAY * habit.frequency.interval(),
                    type = AlarmManager.RTC_WAKEUP
                )
            }
    }
}

interface FrequencyInterval {
    fun interval(): Int
}