package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.core.reminder.ReminderScheduler
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
        mapper: HabitUiMapper,
        settings: SettingsCache
    ): NotificationInteractor = NotificationInteractor.Base(repository, scheduler, notificationMapper, mapper, settings)

    @Provides
    fun uiMapper(): HabitUiMapper = HabitUiMapper.Base()
}