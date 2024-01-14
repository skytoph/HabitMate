package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.create.EditHabitState
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object EditHabitViewModelModule {

    @Provides
    fun mapper(convertIcon: ConvertIcon, now: Now): HabitDomainMapper =
        HabitDomainMapper.Base(convertIcon, now)
}

@Module
@InstallIn(SingletonComponent::class)
object EditHabitStateModule {

    @Provides
    @Singleton
    fun state(): MutableState<EditHabitState> = mutableStateOf(EditHabitState())
}