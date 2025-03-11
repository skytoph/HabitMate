package com.skytoph.taski.presentation.habit.create

import android.content.Context
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface CreateHabitInteractor {
    suspend fun insert(habit: HabitUi, context: Context, isFirstDaySunday: Boolean)

    class Base(
        private val repository: HabitRepository,
        private val mapper: HabitDomainMapper,
        notificationInteractor: NotificationInteractor,
    ) : CreateHabitInteractor, NotificationInteractor by notificationInteractor {

        override suspend fun insert(habit: HabitUi, context: Context, isFirstDaySunday: Boolean) {
            val id = repository.insert(habit.map(mapper, context))
            scheduleNotification(habit.copy(id = id), context, isFirstDaySunday)
        }
    }
}