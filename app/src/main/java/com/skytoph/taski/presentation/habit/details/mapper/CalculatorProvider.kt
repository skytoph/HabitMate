package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.core.Now
import com.skytoph.taski.presentation.habit.details.streak.CalculateCustomStreak
import com.skytoph.taski.presentation.habit.details.streak.CalculateDailyStreak
import com.skytoph.taski.presentation.habit.details.streak.CalculateEverydayStreak
import com.skytoph.taski.presentation.habit.details.streak.CalculateMonthlyStreak
import com.skytoph.taski.presentation.habit.details.streak.CalculateStreak

interface CalculatorProvider {
    fun provideEveryday(isFirstDaySunday: Boolean): CalculateStreak
    fun provideMonthly(isFirstDaySunday: Boolean, isEveryday: Boolean, days: Set<Int>): CalculateStreak
    fun provideDaily(isFirstDaySunday: Boolean, isEveryday: Boolean, days: Set<Int>): CalculateStreak
    fun provideCustomDaily(isFirstDaySunday: Boolean, isEveryday: Boolean, times: Int, type: Int): CalculateStreak
    fun provideCustomWeekly(isFirstDaySunday: Boolean, isEveryday: Boolean, times: Int, type: Int): CalculateStreak
    fun provideCustomMonthly(isFirstDaySunday: Boolean, isEveryday: Boolean, times: Int, type: Int): CalculateStreak

    class Base : CalculatorProvider {

        override fun provideEveryday(isFirstDaySunday: Boolean) = CalculateEverydayStreak()

        override fun provideDaily(isFirstDaySunday: Boolean, isEveryday: Boolean, days: Set<Int>) =
            if (isEveryday) provideEveryday(isFirstDaySunday)
            else CalculateDailyStreak(provideNow(isFirstDaySunday), days)

        override fun provideMonthly(isFirstDaySunday: Boolean, isEveryday: Boolean, days: Set<Int>) =
            if (isEveryday) provideEveryday(isFirstDaySunday)
            else CalculateMonthlyStreak(provideNow(isFirstDaySunday), days)

        override fun provideCustomDaily(isFirstDaySunday: Boolean, isEveryday: Boolean, times: Int, type: Int) = when {
            isEveryday -> CalculateEverydayStreak()
            type % 7 == 0 -> CalculateCustomStreak.Week(times, type / 7, provideNow(isFirstDaySunday))
            else -> CalculateCustomStreak.Day(times, type)
        }

        override fun provideCustomWeekly(isFirstDaySunday: Boolean, isEveryday: Boolean, times: Int, type: Int) =
            if (isEveryday) CalculateEverydayStreak()
            else CalculateCustomStreak.Week(times, type, provideNow(isFirstDaySunday))

        override fun provideCustomMonthly(isFirstDaySunday: Boolean, isEveryday: Boolean, times: Int, type: Int) =
            if (isEveryday) CalculateEverydayStreak()
            else CalculateCustomStreak.Month(times, type, provideNow(isFirstDaySunday))

        private fun provideNow(isFirstDaySunday: Boolean): Now = Now.Base(isFirstDaySunday)
    }
}