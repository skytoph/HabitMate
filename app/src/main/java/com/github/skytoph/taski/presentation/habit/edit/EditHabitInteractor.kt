package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface EditHabitInteractor : HabitDoneInteractor, NotificationInteractor {
    suspend fun habit(id: Long): Habit
    suspend fun insert(habit: HabitUi, context: Context)

    class Base(
        private val mapper: HabitDomainMapper,
        private val repository: HabitRepository,
        private val scheduler: AlarmScheduler,
        private val notificationMapper: HabitNotificationMapper,
        rescheduleMapper: AddMonthMapper,
        now: Now,
    ) : EditHabitInteractor, HabitDoneInteractor by HabitDoneInteractor.Base(repository, now),
        NotificationInteractor by
        NotificationInteractor.Base(repository, scheduler, notificationMapper, rescheduleMapper) {

        override suspend fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: HabitUi, context: Context) {
            scheduler.cancel(context, habit.id, habit.frequency.times)
            repository.update(habit.map(mapper, context))
        }

        override fun scheduleNotification(habit: HabitUi, context: Context) =
            habit.frequency.schedule(scheduler, context, notificationMapper.map(habit, context))
    }
}