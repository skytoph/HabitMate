package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.details.mapper.CalculatorProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatsUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object StatisticsModule {

    @Provides
    @ViewModelScoped
    fun mapper(provider: CalculatorProvider): HabitStatsUiMapper = HabitStatsUiMapper.Base(provider)

    @Provides
    @ViewModelScoped
    fun provider(now: Now): CalculatorProvider = CalculatorProvider.Base(now)
}