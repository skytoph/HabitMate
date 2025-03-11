package com.skytoph.taski.presentation.habit

import com.skytoph.taski.domain.habit.Reminder
import java.util.Locale

data class ReminderUi(
    val switchedOn: Boolean = false,
    val hour: Int = 12,
    val minute: Int = 0,
    val isDialogShown: Boolean = false
) {
    fun formatted(locale: Locale, turnedOff: String, is24HourFormat: Boolean, isAllowed: Boolean = true): String =
        if (switchedOn && isAllowed) {
            val hourFormated =
                if (!is24HourFormat) when {
                    hour == 0 -> 12
                    hour > 12 -> hour - 12
                    else -> hour
                }
                else hour
            val format = "%02d:%02d" +
                    if (is24HourFormat) ""
                    else " " + if (is24HourFormat) null else if (hour >= 12) "PM" else "AM"
            String.format(locale, format, hourFormated, minute)
        } else turnedOff

    fun map(): Reminder = if (switchedOn) Reminder.SwitchedOn(hour = hour, minute = minute) else Reminder.None
}