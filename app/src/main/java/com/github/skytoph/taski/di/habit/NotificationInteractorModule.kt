package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.alarm.ReminderScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationInteractorModule {

    @Provides
    fun interactor(
        scheduler: ReminderScheduler,
        notificationMapper: HabitNotificationMapper,
        repository: HabitRepository,
        mapper: HabitUiMapper
    ): NotificationInteractor = NotificationInteractor.Base(repository, scheduler, notificationMapper, mapper)

    @Provides
    fun uiMapper(): HabitUiMapper = HabitUiMapper.Base()
}