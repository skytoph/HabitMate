package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.details.HabitStatistics
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi

interface StatisticsUiMapper {
    fun map(data: HabitWithEntries, isFirstDaySunday: Boolean): HabitStatisticsUi

    class Base(private val mapper: HabitStatisticsMapper) : StatisticsUiMapper {
        override fun map(data: HabitWithEntries, isFirstDaySunday: Boolean): HabitStatisticsUi =
            mapper.map(data, isFirstDaySunday).map()
    }
}

fun HabitStatistics.mapToUi(): HabitStatisticsUi = HabitStatisticsUi(
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    total = total,
    streaks = streaks.map { it.map() }
)