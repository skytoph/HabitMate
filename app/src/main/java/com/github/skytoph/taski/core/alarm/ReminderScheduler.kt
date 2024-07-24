package com.github.skytoph.taski.core.alarm

import android.content.Context
import android.net.Uri

interface ReminderScheduler {
    fun scheduleRepeating(context: Context, items: List<AlarmItem>)
    fun schedule(context: Context, items: List<AlarmItem>)
    fun cancel(context: Context, uri: Uri)
    fun cancel(context: Context, id: Long, times: Int)
}

interface ScheduleReminder {
    fun schedule(scheduler: ReminderScheduler, context: Context, items: List<AlarmItem>)
}