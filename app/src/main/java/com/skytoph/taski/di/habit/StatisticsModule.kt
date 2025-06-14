package com.skytoph.taski.di.habit

import com.skytoph.taski.presentation.habit.details.mapper.CalculatorProvider
import com.skytoph.taski.presentation.habit.details.mapper.HabitStateMapper
import com.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper
import com.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {

    @Provides
    fun statsMapper(provider: CalculatorProvider): HabitStatisticsMapper =
        HabitStatisticsMapper(provider)

    @Provides
    fun uiMapper(mapper: HabitStatisticsMapper, stateMapper: HabitStateMapper): StatisticsUiMapper =
        StatisticsUiMapper.Base(mapper, stateMapper)
}