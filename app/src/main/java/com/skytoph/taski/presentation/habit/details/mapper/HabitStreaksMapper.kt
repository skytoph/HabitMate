package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.presentation.habit.details.HabitStatistics
import com.skytoph.taski.presentation.habit.details.streak.CalculateStreak

class HabitStreaksMapper(provider: CalculatorProvider) :
    HabitStreaksResultMapper.Abstract<HabitStatistics>(provider) {

    override val action: CalculateStreak.(Map<Int, Entry>, Int) -> HabitStatistics =
        CalculateStreak::streaks
}