package com.skytoph.taski.core.reminder

import android.app.NotificationManager

sealed class HabitNotificationChannel(
    val name: String,
    val id: String,
    val priority: Int
) {

    data object HabitReminder : HabitNotificationChannel("notifications", "habit_alarm_id", NotificationManager.IMPORTANCE_HIGH)

    data object RefreshReminder :
        HabitNotificationChannel("refresh reminders", "refresh_reminder_id", NotificationManager.IMPORTANCE_MIN)
}