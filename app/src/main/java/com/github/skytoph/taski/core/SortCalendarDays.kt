package com.github.skytoph.taski.core

object SortCalendarDays {
    fun sort(days: Collection<Int>, isFirstDaySunday: Boolean): Collection<Int> =
        if (isFirstDaySunday) days.sorted()
        else days.sortedBy { (it + 5) % 7 }
}