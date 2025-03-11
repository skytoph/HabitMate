package com.skytoph.taski.di.core

import com.skytoph.taski.presentation.core.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LogModule {

    @Provides
    fun log(): Logger = Logger.Crashlytics()
}