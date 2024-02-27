package com.github.skytoph.taski.presentation.core.format

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

fun getWeekDisplayName(locale: Locale, dayOfWeek: Int): String {
    val calendar = Calendar.getInstance(locale).also {
        it.set(Calendar.DAY_OF_WEEK, it.firstDayOfWeek)
        it.add(Calendar.DAY_OF_WEEK, dayOfWeek)
    }
    return SimpleDateFormat("EE", locale).format(calendar.timeInMillis)
}