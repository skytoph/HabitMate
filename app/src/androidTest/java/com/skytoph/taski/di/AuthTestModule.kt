package com.skytoph.taski.di

import com.skytoph.taski.core.auth.SignInWithGoogle
import com.skytoph.taski.di.core.AuthModule
import com.skytoph.taski.mock.auth.AuthTest
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthModule::class]
)
object AuthTestModule {

    @Provides
    @Singleton
    fun auth(): SignInWithGoogle = AuthTest()
}