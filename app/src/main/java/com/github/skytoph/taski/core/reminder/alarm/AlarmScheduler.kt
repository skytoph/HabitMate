package com.github.skytoph.taski.core.reminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.reminder.HabitUriConverter
import com.github.skytoph.taski.core.reminder.ReminderItem
import com.github.skytoph.taski.core.reminder.ReminderScheduler
import com.google.gson.Gson
import java.text.SimpleDateFormat

class AlarmScheduler(
    private val alarm: AlarmProvider,
    private val uriConverter: HabitUriConverter,
    private val gson: Gson,
    private val context: Context
) : ReminderScheduler {

    override fun scheduleRepeating(items: List<ReminderItem>) = schedule(items)

    override fun schedule(items: List<ReminderItem>) {
        val alarmManager = alarm.alarmManager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) return
        items.forEach { item ->
            Log.e(
                AlarmScheduler::class.simpleName,
                "reminder scheduled: " + SimpleDateFormat(context.getString(R.string.backup_date_format)).format(item.timeMillis)
            )
            alarmManager.setExactAndAllowWhileIdle(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ item.timeMillis,
                /* operation = */ alarmIntent(item)
            )
        }
    }

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
        return alarm.alarmIntent(context, intent, item.id.toInt())
    }

    private fun intent(uri: Uri): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.setAction(AlarmReceiver.ACTION)
        intent.setData(uri)
        return intent
    }

    private fun cancelAlarmIntent(uri: Uri): PendingIntent {
        val intent = intent( uri)
        val code = uriConverter.id(uri).toInt()
        return alarm.alarmIntent(context, intent, code)
    }
}