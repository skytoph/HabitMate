package com.github.skytoph.taski.core

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object CalendarProvider {
    private fun getTimeZone(): TimeZone = TimeZone.getDefault()

    fun getCalendar(locale: Locale, isFirstDaySunday: Boolean): Calendar = Calendar.getInstance(getTimeZone(), locale)
        .apply { firstDayOfWeek = if (isFirstDaySunday) Calendar.SUNDAY else Calendar.MONDAY }

    fun getCalendar(isFirstDaySunday: Boolean): Calendar = Calendar.getInstance(TimeZone.getDefault())
        .apply { firstDayOfWeek = if (isFirstDaySunday) Calendar.SUNDAY else Calendar.MONDAY }

    fun getCalendarDefault(isFirstDaySunday: Boolean): Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        .apply { firstDayOfWeek = if (isFirstDaySunday) Calendar.SUNDAY else Calendar.MONDAY }
}
