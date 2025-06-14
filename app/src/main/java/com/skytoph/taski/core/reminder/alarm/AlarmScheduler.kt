package com.skytoph.taski.core.reminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.google.gson.Gson
import com.skytoph.taski.R
import com.skytoph.taski.core.reminder.HabitUriConverter
import com.skytoph.taski.core.reminder.ReminderItem
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.presentation.core.Logger
import java.text.SimpleDateFormat

class AlarmScheduler(
    private val alarm: AlarmProvider,
    private val uriConverter: HabitUriConverter,
    private val gson: Gson,
    private val context: Context,
    private val log: Logger
) : ReminderScheduler {

    override fun scheduleRepeating(items: List<ReminderItem>) = schedule(items)

    override fun schedule(items: List<ReminderItem>) {
        val alarmManager = alarm.alarmManager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) return
        items.forEach { item ->
            log.logDebug(
                "reminder scheduled: " + SimpleDateFormat(context.getString(R.string.backup_date_format_24h_format)).format(
                    item.timeMillis
                ), AlarmScheduler::class.simpleName.toString()
            )
            alarmManager.setExactAndAllowWhileIdle(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ item.timeMillis,
                /* operation = */ alarmIntent(item)
            )
        }
    }

    override fun areNotificationsAllowed(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarm.alarmManager(context).canScheduleExactAlarms()

    override fun reschedule(item: ReminderItem) =
        schedule(items = listOf(item.copy(timeMillis = item.interval.next(item.timeMillis, item.day))))

    override fun cancel(id: Long, times: Int) =
        (0 until times).forEach { index ->
            cancel(uriConverter.uri(id, index))
        }

    override fun cancel(uri: Uri) {
        val pendingIntent = cancelAlarmIntent(uri)
        alarm.alarmManager(context).cancel(pendingIntent)
    }

    private fun alarmIntent(item: ReminderItem): PendingIntent {
        val intent = intent(uriConverter.uri(item.uri))
        intent.putExtra(ReminderItem.KEY_ITEM, gson.toJson(item))
        return alarm.alarmIntent(context, intent, item.id.hashCode())
    }

    private fun intent(uri: Uri): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.setAction(AlarmReceiver.ACTION)
        intent.setData(uri)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        return intent
    }

    private fun cancelAlarmIntent(uri: Uri): PendingIntent {
        val intent = intent(uri)
        val code = uriConverter.id(uri).hashCode()
        return alarm.alarmIntent(context, intent, code)
    }
}