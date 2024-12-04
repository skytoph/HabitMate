package com.github.skytoph.taski.di.core

import android.content.Context
import com.github.skytoph.taski.core.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkManagerModule {

    @Provides
    fun manager(@ApplicationContext context: Context): NetworkManager = NetworkManager.Base(context)
}