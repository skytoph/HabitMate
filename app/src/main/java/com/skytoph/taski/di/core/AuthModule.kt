package com.skytoph.taski.di.core

import com.skytoph.taski.core.auth.SignInWithGoogle
import com.skytoph.taski.presentation.core.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun auth(log: Logger): SignInWithGoogle = SignInWithGoogle.Base(log)
}