package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateStreak

interface HabitStatsUiMapper {
    fun map(data: HabitWithEntries): HabitStatisticsUi

    class Base(private val provider: CalculatorProvider) : HabitStatsUiMapper {
        override fun map(data: HabitWithEntries) =
            data.habit.frequency.map(object : FrequencyMapper {
                override fun map(days: Set<Int>) =
                    provider.provide(data.habit.frequency).let { calculator ->
                        HabitStatisticsUi(
                            currentStreak = calculator.currentStreak(
                                data = data.entries.entries, goal = data.habit.goal,
                            ),
                            bestStreak = calculator.maxStreak(
                                data = data.entries.entries, goal = data.habit.goal,
                            ),
                            total = calculator.total(
                                data = data.entries.entries, goal = data.habit.goal,
                            )
                        )
                    }
            })
    }
}

interface CalculatorProvider {
    fun provide(frequency: Frequency): CalculateStreak
}

interface FrequencyMapper {
    fun map(days: Set<Int> = emptySet()): HabitStatisticsUi
}