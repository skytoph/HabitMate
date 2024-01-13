package com.github.skytoph.taski.di.core

import com.github.skytoph.taski.core.Now
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CurrentTimeProviderModule {

    @Provides
    fun now(): Now = Now.Base()
}