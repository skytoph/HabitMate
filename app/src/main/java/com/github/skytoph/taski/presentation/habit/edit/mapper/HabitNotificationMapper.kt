package com.github.skytoph.taski.presentation.habit.edit.mapper

import android.content.Context
import androidx.compose.ui.graphics.toArgb
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
                    id = habit.id,
                    title = habit.title,
                    message = R.string.habit_reminder_message,
                    icon = habit.icon.name(context.resources),
                    timeMillis = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
                        set(Calendar.MINUTE, habit.reminder.minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis,
                    day = day,
                    color = habit.color.toArgb(),
                    interval = habit.frequency.interval,
                    uri = uriConverter.uri(habit.id, index).toString()
                )
            }
    }
}

interface FrequencyInterval {
    val interval: Int

    companion object {
        const val INTERVAL_MONTH = -31
        const val INTERVAL_NOT_SUPPORTED = -1
    }
}