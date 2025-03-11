package com.skytoph.taski.presentation.core.component

fun weekDayCalendar(isFirstDaySunday: Boolean, index: Int) =
    if (isFirstDaySunday) index else index % 7 + 1