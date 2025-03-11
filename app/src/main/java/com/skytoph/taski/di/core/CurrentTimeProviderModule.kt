package com.skytoph.taski.di.core

import com.skytoph.taski.core.Now
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrentTimeProviderModule {

    @Provides
    @Singleton
    fun now(): Now = Now.Base()
}