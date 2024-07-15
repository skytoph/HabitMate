package com.github.skytoph.taski.presentation.core.format

import android.icu.util.Calendar
import com.github.skytoph.taski.core.CalendarProvider
import java.text.SimpleDateFormat
import java.util.Locale

fun getWeekDisplayName(locale: Locale, dayOfWeek: Int): String {
    val calendar = CalendarProvider.getCalendar(locale, true).also {
        it.set(Calendar.DAY_OF_WEEK, dayOfWeek)
    }
    return SimpleDateFormat("EE", locale).format(calendar.timeInMillis)
}

fun getDayOfWeek(locale: Locale, daysAgo: Int = 0): Int {
    return CalendarProvider.getCalendar(locale, true).apply { add(Calendar.DATE, -daysAgo) }.get(Calendar.DAY_OF_WEEK)
}

fun getTodayDayOfMonth(locale: Locale, days: Int): Int {
    val calendar = CalendarProvider.getCalendar(locale, false)
    calendar.add(Calendar.DAY_OF_WEEK, days)
    return calendar.get(Calendar.DAY_OF_MONTH)
}