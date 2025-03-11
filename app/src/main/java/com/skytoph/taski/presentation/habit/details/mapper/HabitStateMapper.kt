package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.presentation.habit.details.HabitState
import com.skytoph.taski.presentation.habit.details.streak.CalculateStreak

class HabitStateMapper(provider: CalculatorProvider) :
    HabitStatisticsResultMapper.Abstract<HabitState>(provider) {

    override val action: CalculateStreak.(Map<Int, Entry>, Int) -> HabitState =
        CalculateStreak::currentState
}