package com.github.skytoph.taski.presentation.habit.edit

import com.github.skytoph.taski.core.alarm.AlarmItem
import java.util.Calendar

interface AddMonthMapper {
    fun addMonth(item: AlarmItem): Calendar

    class Base : AddMonthMapper {
        override fun addMonth(item: AlarmItem): Calendar = item.calendar.apply {
            val daysInMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
            val day = get(Calendar.DAY_OF_MONTH)
            if (day == item.day)
                add(Calendar.DATE, daysInMonth)
            else if (item.day > daysInMonth)
                add(Calendar.DATE, item.day - day)
            else
                set(Calendar.DAY_OF_MONTH, item.day)
        }
    }
}
