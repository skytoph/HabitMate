package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.presentation.habit.details.mapper.CalculatorProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
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
    fun statsMapper(provider: CalculatorProvider): HabitStatisticsMapper =
        HabitStatisticsMapper(provider)

    @Provides
    @ViewModelScoped
    fun uiMapper(mapper: HabitStatisticsMapper): StatisticsUiMapper =
        StatisticsUiMapper.Base(mapper)
}