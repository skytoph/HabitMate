package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.habit.details.HabitStatisticsResult
import com.skytoph.taski.presentation.habit.details.streak.CalculateStreak

interface HabitStatisticsResultMapper<T : HabitStatisticsResult> {
    fun map(data: HabitWithEntries, isFirstDaySunday: Boolean): T

    abstract class Abstract<T : HabitStatisticsResult>(private val provider: CalculatorProvider) :
        HabitStatisticsResultMapper<T> {

        override fun map(data: HabitWithEntries, isFirstDaySunday: Boolean): T =
            data.habit.frequency.map(object : FrequencyMapper<T> {
                override fun map(days: Set<Int>): T {
                    val calculator = data.habit.frequency.provide(provider, isFirstDaySunday)
                    val entries = data.entries.entries.toSortedMap()
                    val goal = data.habit.goal
                    return calculator.action(entries, goal)
                }
            })

        abstract val action: CalculateStreak.(Map<Int, Entry>, Int) -> T
    }
}

interface FrequencyMapper<T : HabitStatisticsResult> {
    fun map(days: Set<Int> = emptySet()): T
}