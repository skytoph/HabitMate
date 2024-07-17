package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.CalendarProvider
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.core.SortCalendarDays
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import java.util.Calendar

class CalculateDailyStreak(
    private val now: Now,
    days: Set<Int>,
) : CalculateStreak.Iterable(SortCalendarDays.sort(days, now.default).toSet()),
    CalculateInterval by CalculateInterval.Week(now) {

    override val maxDays: Int = 7

    override fun dayNumber(daysAgo: Int): Int = CalendarProvider.getCalendarDefault(FrequencyUi.NOW_DEFAULT)
        .apply { add(Calendar.DATE, -daysAgo) }.get(Calendar.DAY_OF_WEEK)

    override fun transform(day: Int): Int = if (now.default) day else (day + 5) % 7 + 1
}