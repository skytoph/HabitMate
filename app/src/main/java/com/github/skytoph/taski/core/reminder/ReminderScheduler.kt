package com.github.skytoph.taski.core.reminder

import android.content.Context
import android.net.Uri

interface ReminderScheduler {
    fun scheduleRepeating(items: List<ReminderItem>)
    fun schedule(items: List<ReminderItem>)
    fun reschedule(item: ReminderItem)
    fun cancel(uri: Uri)
    fun cancel(id: Long, times: Int)
    fun areNotificationsAllowed(context: Context): Boolean
}

interface ScheduleReminder {
    fun schedule(scheduler: ReminderScheduler, items: List<ReminderItem>)
}