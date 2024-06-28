package com.github.skytoph.taski.presentation.habit.edit.mapper

import android.app.AlarmManager
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.presentation.habit.HabitUi
import java.util.Calendar

interface HabitNotificationMapper {
    fun map(habit: HabitUi): AlarmItem

    class Base : HabitNotificationMapper {
        override fun map(habit: HabitUi) = AlarmItem(
            title = habit.title,
            message = R.string.habit_reminder_message,
            icon = habit.icon,
            calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
                set(Calendar.MINUTE, habit.reminder.minute)
            },
            interval = AlarmManager.INTERVAL_DAY,
            type = AlarmManager.RTC_WAKEUP
        )
    }
}
