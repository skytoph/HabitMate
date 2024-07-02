package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.HabitStatistics
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateStreak

class HabitStatisticsMapper(provider: CalculatorProvider) :
    HabitStatisticsResultMapper.Abstract<HabitStatistics>(provider) {

    override val action: CalculateStreak.(Map<Int, Entry>, Int) -> HabitStatistics =
        CalculateStreak::streaks
}