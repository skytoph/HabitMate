package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateCustomStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateDailyStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateEverydayStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateMonthlyStreak
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateStreak

interface CalculatorProvider {
    fun provideEveryday(): CalculateStreak
    fun provideMonthly(isEveryday: Boolean, days: Set<Int>): CalculateStreak
    fun provideDaily(isEveryday: Boolean, days: Set<Int>): CalculateStreak
    fun provideCustomDaily(isEveryday: Boolean, times: Int, type: Int): CalculateStreak
    fun provideCustomWeekly(isEveryday: Boolean, times: Int, type: Int): CalculateStreak
    fun provideCustomMonthly(isEveryday: Boolean, times: Int, type: Int): CalculateStreak

    class Base(private val now: Now) : CalculatorProvider {

        override fun provideEveryday() = CalculateEverydayStreak()

        override fun provideDaily(isEveryday: Boolean, days: Set<Int>) =
            if (isEveryday) provideEveryday() else CalculateDailyStreak(now, days)

        override fun provideMonthly(isEveryday: Boolean, days: Set<Int>) =
            if (isEveryday) provideEveryday() else CalculateMonthlyStreak(now, days)

        override fun provideCustomDaily(isEveryday: Boolean, times: Int, type: Int) = when {
            isEveryday -> CalculateEverydayStreak()
            type % 7 == 0 -> CalculateCustomStreak.Week(times, type / 7, now)
            else -> CalculateCustomStreak.Day(times, type)
        }

        override fun provideCustomWeekly(isEveryday: Boolean, times: Int, type: Int) =
            if (isEveryday) CalculateEverydayStreak()
            else CalculateCustomStreak.Week(times, type, now)

        override fun provideCustomMonthly(isEveryday: Boolean, times: Int, type: Int) =
            if (isEveryday) CalculateEverydayStreak()
            else CalculateCustomStreak.Month(times, type, now)
    }
}