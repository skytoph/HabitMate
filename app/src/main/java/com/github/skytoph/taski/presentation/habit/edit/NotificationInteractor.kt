package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.core.alarm.ReminderScheduler
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.domain.habit.CheckHabitState
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper

interface NotificationInteractor {
    fun scheduleNotification(habit: HabitUi, context: Context, isFirstDaySunday: Boolean)
    suspend fun refreshAllNotifications(context: Context, isFirstDaySunday: Boolean)

    class Base(
        private val repository: HabitRepository,
        private val scheduler: ReminderScheduler,
        private val notificationMapper: HabitNotificationMapper,
        private val mapper: HabitUiMapper,
    ) : NotificationInteractor {

        override fun scheduleNotification(habit: HabitUi, context: Context, isFirstDaySunday: Boolean) {
            if (habit.reminder.switchedOn)
                habit.frequency.schedule(scheduler, context, notificationMapper.map(habit, context, isFirstDaySunday))
        }

        override suspend fun refreshAllNotifications(context: Context, isFirstDaySunday: Boolean) {
            repository.habits().forEach { habit -> scheduleNotification(mapper.map(habit), context, isFirstDaySunday) }
        }
    }
}

interface NotificationStateInteractor : CheckHabitState {
    fun rescheduleNotification(item: AlarmItem, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val scheduler: ReminderScheduler,
        private val settings: SettingsCache,
    ) : NotificationStateInteractor {

        override fun rescheduleNotification(item: AlarmItem, context: Context) {
            if (item.interval.reschedule) scheduler.schedule(
                context = context, items = listOf(item.copy(timeMillis = item.interval.next(item.timeMillis, item.day)))
            )
        }

        override suspend fun notCompleted(habitId: Long, isFirstDaySunday: Boolean): Boolean =
            repository.notCompleted(habitId, settings.state().value.weekStartsOnSunday.value)

        override suspend fun habit(id: Long): Habit = repository.habit(id)
    }
}