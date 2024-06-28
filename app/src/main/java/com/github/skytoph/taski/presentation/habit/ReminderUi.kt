package com.github.skytoph.taski.presentation.habit

import com.github.skytoph.taski.domain.habit.Reminder
import java.util.Locale

data class ReminderUi(
    val switchedOn: Boolean = false,
    val hour: Int = 12,
    val minute: Int = 0,
    val isDialogShown: Boolean = false
) {
    fun formatted(locale: Locale): String {
        return String.format(locale, "%02d:%02d", hour, minute)
    }

    fun map(): Reminder =
        if (switchedOn) Reminder.SwitchedOn(hour = hour, minute = minute) else Reminder.None
}