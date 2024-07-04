package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.domain.habit.CheckHabitState
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.FrequencyInterval
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper

interface NotificationInteractor : CheckHabitState {
    fun scheduleNotification(habit: HabitUi, context: Context)
    fun rescheduleNotification(item: AlarmItem, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val scheduler: AlarmScheduler,
        private val notificationMapper: HabitNotificationMapper,
        private val rescheduleMapper: AddMonthMapper,
    ) : NotificationInteractor {

        override fun scheduleNotification(habit: HabitUi, context: Context) =
            habit.frequency.schedule(scheduler, context, notificationMapper.map(habit, context))

        override fun rescheduleNotification(item: AlarmItem, context: Context) {
            if (!item.intervalEquals(FrequencyInterval.INTERVAL_MONTH)) return
            val calendar = rescheduleMapper.addMonth(item)
            scheduler.schedule(context, listOf(item.copy(calendar = calendar)))
        }

        override suspend fun isHabitDone(habitId: Long): Boolean = repository.isHabitDone(habitId)
    }
}