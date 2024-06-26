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
                        val entries = data.entries.entries.toSortedMap()
                        val goal = data.habit.goal
                        val statistics = calculator.streaks(data = entries, goal = goal)
                        statistics.map()
                    }
            })
    }
}

data class HabitStatistics(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val total: Int = 0,
    val streaks: List<Streak> = emptyList(),
)

data class Streak(val start: Int, val end: Int, val streak: Int)

interface CalculatorProvider {
    fun provide(frequency: Frequency): CalculateStreak
}

interface FrequencyMapper {
    fun map(days: Set<Int> = emptySet()): HabitStatisticsUi
}