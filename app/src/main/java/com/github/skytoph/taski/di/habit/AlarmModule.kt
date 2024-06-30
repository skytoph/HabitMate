package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.alarm.AlarmProvider
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitDateMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AlarmModule {

    @Provides
    @ViewModelScoped
    fun alertMapper(mapper: HabitDateMapper): HabitNotificationMapper =
        HabitNotificationMapper.Base(mapper)

    @Provides
    @ViewModelScoped
    fun dateMapper(): HabitDateMapper = HabitDateMapper.Base()

    @Provides
    @ViewModelScoped
    fun scheduler(alarm: AlarmProvider): AlarmScheduler = AlarmScheduler.Base(alarm)

    @Provides
    @ViewModelScoped
    fun provider(): AlarmProvider = AlarmProvider.Base()
}
