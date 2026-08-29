package com.skytoph.taski.di

import com.skytoph.taski.di.habit.RewardModule
import com.skytoph.taski.mock.datastore.TestRewardDataSource
import com.skytoph.taski.presentation.habit.icon.RewardDataSource
import com.skytoph.taski.presentation.habit.icon.RewardFailResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RewardModule::class]
)
object TestRewardModule {

    @Provides
    @Singleton
    fun rewards(error: RewardFailResult?): RewardDataSource = TestRewardDataSource(error = error)
}

@Module
@InstallIn(SingletonComponent::class)
object TestRewardErrorModule {

    @Provides
    fun error(): RewardFailResult? = null
}