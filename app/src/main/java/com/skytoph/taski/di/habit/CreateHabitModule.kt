package com.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.skytoph.taski.BuildConfig
import com.skytoph.taski.presentation.habit.create.CreateHabitEvent
import com.skytoph.taski.presentation.habit.create.CreateHabitState
import com.skytoph.taski.presentation.habit.create.HabitValidator
import com.skytoph.taski.presentation.habit.create.NewHabitValidator
import com.skytoph.taski.presentation.habit.create.TestValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CreateHabitModule {

    @Provides
    fun state(): MutableState<CreateHabitState> = mutableStateOf(CreateHabitState())

    @Provides
    fun validator(): HabitValidator<CreateHabitEvent> =
        if (BuildConfig.BUILD_TYPE.contentEquals("benchmark")) TestValidator()
        else NewHabitValidator()
}