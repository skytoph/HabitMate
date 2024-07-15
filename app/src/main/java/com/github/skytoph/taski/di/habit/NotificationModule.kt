package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.core.alarm.HabitUriConverter
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.edit.AddMonthMapper
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
    fun interactor(
        repository: HabitRepository,
        scheduler: AlarmScheduler,
        rescheduleMapper: AddMonthMapper
    ): NotificationStateInteractor = NotificationStateInteractor.Base(repository, scheduler, rescheduleMapper)

    @Provides
    fun rescheduleMapper(): AddMonthMapper = AddMonthMapper.Base()

    @Provides
    fun alertMapper(mapper: HabitDateMapper, uriConverter: HabitUriConverter)
            : HabitNotificationMapper = HabitNotificationMapper.Base(mapper, uriConverter)

    @Provides
    fun dateMapper(): HabitDateMapper = HabitDateMapper.Base()
}