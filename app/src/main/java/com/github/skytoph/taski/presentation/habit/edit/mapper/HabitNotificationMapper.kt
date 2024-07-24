package com.github.skytoph.taski.presentation.habit.edit.mapper

import android.content.Context
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.core.alarm.HabitUriConverter
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.HabitUi
import java.util.Calendar

interface HabitNotificationMapper {
    fun map(habit: HabitUi, context: Context, isFirstDaySunday: Boolean): List<AlarmItem>

    class Base(
        private val dateMapper: HabitDateMapper,
        private val uriConverter: HabitUriConverter
    ) : HabitNotificationMapper {
        override fun map(habit: HabitUi, context: Context, isFirstDaySunday: Boolean): List<AlarmItem> =
            habit.frequency.dates(dateMapper, isFirstDaySunday).toList().mapIndexed { index, (day, calendar) ->
                AlarmItem(
                    id = habit.id,
                    messageIdentifier = StringResource.IdentifierFromId(R.string.habit_reminder_message)
                        .getIdentifier(context),
                    timeMillis = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
                        set(Calendar.MINUTE, habit.reminder.minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis,
                    day = day,
                    interval = habit.frequency.interval,
                    uri = uriConverter.uri(habit.id, index).toString(),
                )
            }
    }
}