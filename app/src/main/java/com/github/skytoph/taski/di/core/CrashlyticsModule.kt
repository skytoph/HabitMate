package com.github.skytoph.taski.di.core

import com.github.skytoph.taski.core.crashlytics.Crashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CrashlyticsModule {

    @Provides
    @Singleton
    fun crashlytics(): Crashlytics = Crashlytics.Base()
}