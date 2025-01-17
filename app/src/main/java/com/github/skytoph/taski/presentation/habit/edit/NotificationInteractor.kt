package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.core.reminder.ReminderItem
import com.github.skytoph.taski.core.reminder.ReminderScheduler
import com.github.skytoph.taski.domain.habit.CheckHabitState
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.Reminder
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import kotlinx.coroutines.flow.first

interface NotificationInteractor {
    fun scheduleNotification(habit: HabitUi, context: Context, isFirstDaySunday: Boolean)
    suspend fun refreshAllNotifications(context: Context)

    class Base(
        private val repository: HabitRepository,
        private val scheduler: ReminderScheduler,
        private val notificationMapper: HabitNotificationMapper,
        private val mapper: HabitUiMapper,
        private val settings: SettingsCache,
    ) : NotificationInteractor {

        override fun scheduleNotification(habit: HabitUi, context: Context, isFirstDaySunday: Boolean) {
            if (habit.reminder.switchedOn)
                habit.frequency.schedule(scheduler, notificationMapper.map(habit, context, isFirstDaySunday))
        }

        override suspend fun refreshAllNotifications(context: Context) {
            val isNotificationEnabled = scheduler.areNotificationsAllowed(context)
            val isFirstDaySunday = settings.initAndGet().first().weekStartsOnSunday.value
            repository.habits().forEach { habit ->
                scheduler.cancel(habit.id, habit.frequency.times)
                if (isNotificationEnabled) repository.update(habit.copy(reminder = Reminder.None))
                else scheduleNotification(mapper.map(habit), context, isFirstDaySunday)
            }
        }
    }
}

interface NotificationStateInteractor : CheckHabitState {
    fun rescheduleNotification(item: ReminderItem, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val scheduler: ReminderScheduler,
        private val settings: SettingsCache,
    ) : NotificationStateInteractor {

        override fun rescheduleNotification(item: ReminderItem, context: Context) =
            scheduler.reschedule(item = item)

        override suspend fun notCompleted(habitId: Long, isFirstDaySunday: Boolean): Boolean =
            repository.notCompleted(habitId, settings.initAndGet().first().weekStartsOnSunday.value)

        override suspend fun habit(id: Long): Habit = repository.habit(id)
    }
}