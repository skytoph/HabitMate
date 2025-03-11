package com.skytoph.taski.di.habit

import com.skytoph.taski.core.reminder.alarm.AlarmProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    fun provider(): AlarmProvider = AlarmProvider.Base()
}