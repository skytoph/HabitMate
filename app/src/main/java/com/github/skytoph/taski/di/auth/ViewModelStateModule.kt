package com.github.skytoph.taski.di.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.presentation.auth.signin.SignInState
import com.github.skytoph.taski.presentation.auth.signup.SignUpState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelStateModule {

    @Provides
    fun signInState(): MutableState<SignInState> = mutableStateOf(SignInState())

    @Provides
    fun signUpState(): MutableState<SignUpState> = mutableStateOf(SignUpState())

    @Provides
    fun verificationState(): MutableState<Boolean?> = mutableStateOf(null)
}