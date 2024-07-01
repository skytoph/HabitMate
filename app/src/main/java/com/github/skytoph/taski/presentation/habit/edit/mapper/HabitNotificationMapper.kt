package com.github.skytoph.taski.presentation.habit.edit.mapper

import android.app.AlarmManager
import android.content.Context
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.core.alarm.HabitUriConverter
import com.github.skytoph.taski.presentation.habit.HabitUi
import java.util.Calendar

interface HabitNotificationMapper {
    fun map(habit: HabitUi, context: Context): List<AlarmItem>

    class Base(
        private val dateMapper: HabitDateMapper,
        private val uriConverter: HabitUriConverter
    ) : HabitNotificationMapper {
        override fun map(habit: HabitUi, context: Context): List<AlarmItem> =
            habit.frequency.dates(dateMapper).toList().mapIndexed { index, (day, calendar) ->
                AlarmItem(
                    id = habit.id.toInt(),
                    title = habit.title,
                    message = R.string.habit_reminder_message,
                    icon = habit.icon.name(context.resources),
                    calendar = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
                        set(Calendar.MINUTE, habit.reminder.minute)
                    },
                    day = day,
                    interval = AlarmManager.INTERVAL_DAY * habit.frequency.interval(),
                    type = AlarmManager.RTC_WAKEUP,
                    uri = uriConverter.uri(habit.id, index)
                )
            }
    }
}

interface FrequencyInterval {
    fun interval(): Int
}