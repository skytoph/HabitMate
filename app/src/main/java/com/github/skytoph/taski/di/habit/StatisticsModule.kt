package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.presentation.habit.details.mapper.CalculatorProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatsUiMapper
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateCustomStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateDailyStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateEverydayStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateMonthlyStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateStreak
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
    fun provider(now: Now): CalculatorProvider = object : CalculatorProvider {
        override fun provide(frequency: Frequency): CalculateStreak = when {
            frequency.isEveryday() -> CalculateEverydayStreak()
            frequency is Frequency.Daily -> CalculateDailyStreak(now, frequency.days)
            frequency is Frequency.Monthly -> CalculateMonthlyStreak(now, frequency.days)
            frequency is Frequency.Custom && frequency.type == Frequency.Custom.Type.Day && frequency.typeCount % 7 == 0 ->
                CalculateCustomStreak.Week(frequency.timesCount, frequency.typeCount / 7, now)

            frequency is Frequency.Custom -> when (frequency.type) {
                Frequency.Custom.Type.Day ->
                    CalculateCustomStreak.Day(frequency.timesCount, frequency.typeCount)

                Frequency.Custom.Type.Week ->
                    CalculateCustomStreak.Week(frequency.timesCount, frequency.typeCount, now)

                Frequency.Custom.Type.Month ->
                    CalculateCustomStreak.Month(frequency.timesCount, frequency.typeCount, now)
            }

            else -> CalculateEverydayStreak()
        }
    }
}