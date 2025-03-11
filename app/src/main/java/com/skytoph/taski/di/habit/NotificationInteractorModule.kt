package com.skytoph.taski.di.habit

import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
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
        mapper: HabitUiMapper,
        settings: SettingsCache
    ): NotificationInteractor = NotificationInteractor.Base(repository, scheduler, notificationMapper, mapper, settings)

    @Provides
    fun uiMapper(): HabitUiMapper = HabitUiMapper.Base()
}