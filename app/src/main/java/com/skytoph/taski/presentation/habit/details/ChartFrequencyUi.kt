package com.skytoph.taski.presentation.habit.details

import androidx.annotation.StringRes
import com.skytoph.taski.R
import com.skytoph.taski.core.Now
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.details.streak.StartAndEnd
import com.skytoph.taski.presentation.habit.list.view.OptionUi
import com.skytoph.taski.presentation.habit.list.view.ProvideOptionUi

sealed interface ChartFrequencyUi : ProvideOptionUi<ChartFrequencyUi> {
    @get:StringRes
    val label: Int

    @StringRes
    fun timePattern(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int

    fun baseInterval(isFirstDaySunday: Boolean): StartAndEnd
    fun provideCalendar(isFirstDaySunday: Boolean): Now = Now.Base(isFirstDaySunday)
    fun end(index: Int, isFirstDaySunday: Boolean): Int = baseInterval(isFirstDaySunday).end(index)
    fun start(index: Int, isFirstDaySunday: Boolean): Int = baseInterval(isFirstDaySunday).start(index)
    fun index(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int
    override fun matches(item: ChartFrequencyUi): Boolean = this == item
    override fun optionUi(): OptionUi = OptionUi(title = StringResource.ResId(label))

    data object Day : ChartFrequencyUi {
        override val label: Int = R.string.frequency_daily
        override fun end(index: Int, isFirstDaySunday: Boolean): Int = index
        override fun start(index: Int, isFirstDaySunday: Boolean): Int = index
        override fun index(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int = daysAgo

        override fun baseInterval(isFirstDaySunday: Boolean): StartAndEnd =
            StartAndEnd.Week(provideCalendar(isFirstDaySunday))

        override fun timePattern(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int =
            now.dayOfMonths(daysAgo).let { dayOfMonth ->
                when (dayOfMonth) {
                    1 ->
                        if (now.numberOfMonth(now.monthsAgo(daysAgo)) % 3 == 0)
                            R.string.pattern_month_and_year
                        else R.string.pattern_day_and_month

                    7, 14, now.daysInMonth(daysAgo) -> R.string.pattern_day_and_month

                    else -> R.string.pattern_day
                }
            }
    }

    data object Week : ChartFrequencyUi {
        override val label: Int = R.string.frequency_weekly

        override fun index(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int =
            now.weeksAgo(daysAgo, isFirstDaySunday)

        override fun baseInterval(isFirstDaySunday: Boolean): StartAndEnd =
            StartAndEnd.Week(provideCalendar(isFirstDaySunday))

        override fun timePattern(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int {
            val weekOfMonth = now.weekOfMonth(daysAgo, isFirstDaySunday)
            return if (weekOfMonth == 1)
                if (now.numberOfMonth(now.monthsAgo(daysAgo)) % 3 == 0)
                    R.string.pattern_month_and_year
                else R.string.pattern_week_and_month
            else R.string.pattern_week
        }
    }

    data object Month : ChartFrequencyUi {
        override val label: Int = R.string.frequency_monthly

        override fun index(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int =
            now.monthsAgo(daysAgo)

        override fun baseInterval(isFirstDaySunday: Boolean): StartAndEnd =
            StartAndEnd.Month(provideCalendar(isFirstDaySunday))

        override fun timePattern(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int =
            if (now.numberOfMonth(now.monthsAgo(daysAgo)).let { it == 0 || it == 6 }) R.string.pattern_month_and_year
            else R.string.pattern_month
    }

    data object Year : ChartFrequencyUi {
        override val label: Int = R.string.frequency_yearly
        override fun timePattern(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int = R.string.pattern_year_full
        override fun index(daysAgo: Int, now: Now, isFirstDaySunday: Boolean): Int =
            now.yearsAgo(daysAgo)

        override fun baseInterval(isFirstDaySunday: Boolean): StartAndEnd =
            StartAndEnd.Year(provideCalendar(isFirstDaySunday))
    }

    companion object {
        val options = listOf(Day, Week, Month, Year)
    }
}