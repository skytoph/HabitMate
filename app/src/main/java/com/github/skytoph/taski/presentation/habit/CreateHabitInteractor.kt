package com.github.skytoph.taski.presentation.habit

import android.content.Context
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.edit.AddMonthMapper
import com.github.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface CreateHabitInteractor {
    suspend fun insert(habit: HabitUi, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val mapper: HabitDomainMapper,
        private val scheduler: AlarmScheduler,
        private val notificationMapper: HabitNotificationMapper,
        rescheduleMapper: AddMonthMapper,
    ) : CreateHabitInteractor,
        NotificationInteractor by
        NotificationInteractor.Base(repository, scheduler, notificationMapper, rescheduleMapper) {

        override suspend fun insert(habit: HabitUi, context: Context) {
            repository.insert(habit.map(mapper, context))
            scheduleNotification(habit, context)
        }
    }
}