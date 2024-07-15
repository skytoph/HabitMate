package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object NotificationInteractorModule {

    @Provides
    fun interactor(scheduler: AlarmScheduler, notificationMapper: HabitNotificationMapper): NotificationInteractor =
        NotificationInteractor.Base(scheduler, notificationMapper)
}