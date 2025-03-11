package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.presentation.habit.details.Streak
import com.skytoph.taski.presentation.habit.details.StreakUi

fun Streak.map() = StreakUi(length = streak, start = start, end = end)