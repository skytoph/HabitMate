package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.core.reminder.HabitUriConverter
import com.github.skytoph.taski.core.reminder.ReminderScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.edit.NotificationStateInteractor
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitDateMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    fun interactor(repository: HabitRepository, scheduler: ReminderScheduler, settings: SettingsCache)
            : NotificationStateInteractor = NotificationStateInteractor.Base(repository, scheduler, settings)

    @Provides
    fun alertMapper(mapper: HabitDateMapper, uriConverter: HabitUriConverter)
            : HabitNotificationMapper = HabitNotificationMapper.Base(mapper, uriConverter)

    @Provides
    fun dateMapper(): HabitDateMapper = HabitDateMapper.Base()
}