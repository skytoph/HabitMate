package com.skytoph.taski.di.habit

import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.habit.icon.RewardDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RewardModule {

    @Provides
    @Singleton
    fun rewards(log: Logger): RewardDataSource = RewardDataSource.Base(log = log)
}