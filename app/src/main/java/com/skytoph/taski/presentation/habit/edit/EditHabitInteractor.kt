package com.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.Habit
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.create.CreateHabitInteractor
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface EditHabitInteractor : HabitDoneInteractor, NotificationInteractor, CreateHabitInteractor {
    suspend fun habit(id: Long): Habit

    class Base(
        private val mapper: HabitDomainMapper,
        private val repository: HabitRepository,
        private val scheduler: ReminderScheduler,
        notificationInteractor: NotificationInteractor,
        habitInteractor: HabitDoneInteractor,
    ) : EditHabitInteractor, HabitDoneInteractor by habitInteractor,
        NotificationInteractor by notificationInteractor {

        override suspend fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: HabitUi, context: Context, isFirstDaySunday: Boolean) {
            val oldHabit = repository.habit(habit.id)
            scheduler.cancel(oldHabit.id, oldHabit.frequency.times)
            repository.update(habit.map(mapper, context))
            scheduleNotification(habit, context, isFirstDaySunday)
        }
    }
}