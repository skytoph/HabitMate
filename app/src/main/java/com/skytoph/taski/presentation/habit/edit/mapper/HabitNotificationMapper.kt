package com.skytoph.taski.presentation.habit.edit.mapper

import android.content.Context
import com.skytoph.taski.R
import com.skytoph.taski.core.reminder.HabitUriConverter
import com.skytoph.taski.core.reminder.ReminderItem
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.HabitUi
import java.util.Calendar

interface HabitNotificationMapper {
    fun map(habit: HabitUi, context: Context, isFirstDaySunday: Boolean): List<ReminderItem>

    class Base(
        private val dateMapper: HabitDateMapper,
        private val uriConverter: HabitUriConverter
    ) : HabitNotificationMapper {
        override fun map(habit: HabitUi, context: Context, isFirstDaySunday: Boolean): List<ReminderItem> =
            habit.frequency.dates(dateMapper, isFirstDaySunday).toList().mapIndexed { index, (day, calendar) ->
                ReminderItem(
                    id = habit.id,
                    messageIdentifier = StringResource.IdentifierFromId(R.string.habit_reminder_message)
                        .getIdentifier(context),
                    timeMillis = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, habit.reminder.hour)
                        set(Calendar.MINUTE, habit.reminder.minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis.let {
                        if (it < System.currentTimeMillis()) habit.frequency.interval.next(it, day)
                        else it
                    },
                    day = day,
                    interval = habit.frequency.interval,
                    uri = uriConverter.uri(habit.id, index).toString(),
                )
            }
    }
}