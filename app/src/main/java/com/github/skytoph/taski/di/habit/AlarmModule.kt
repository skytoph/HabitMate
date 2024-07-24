package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.alarm.AlarmProvider
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
    fun provider(): AlarmProvider = AlarmProvider.Base()
}