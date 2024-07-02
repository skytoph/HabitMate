package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi

interface HabitStatsUiMapper {
    fun map(data: HabitWithEntries): HabitStatisticsUi

    class Base(private val provider: CalculatorProvider) : HabitStatsUiMapper {
        override fun map(data: HabitWithEntries) =
            data.habit.frequency.map(object : FrequencyMapper {
                override fun map(days: Set<Int>) =
                    data.habit.frequency.provide(provider).let { calculator ->
                        val entries = data.entries.entries.toSortedMap()
                        val goal = data.habit.goal
                        val statistics = calculator.streaks(data = entries, goal = goal)
                        statistics.map()
                    }
            })
    }
}

interface FrequencyMapper {
    fun map(days: Set<Int> = emptySet()): HabitStatisticsUi
}