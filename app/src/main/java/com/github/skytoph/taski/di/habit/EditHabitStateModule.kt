package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.presentation.habit.edit.EditHabitState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EditHabitStateModule {

    @Provides
    @Singleton
    fun state(): MutableState<EditHabitState> = mutableStateOf(EditHabitState())
}