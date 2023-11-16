package com.github.skytoph.taski.di.auth

import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import com.github.skytoph.taski.data.auth.repository.BaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun authRepository(): AuthRepository = BaseAuthRepository(FirebaseAuth.getInstance())
}