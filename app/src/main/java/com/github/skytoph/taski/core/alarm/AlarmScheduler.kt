package com.github.skytoph.taski.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

class AlarmScheduler(
    private val alarm: AlarmProvider,
    private val uriConverter: HabitUriConverter,
) : ReminderScheduler {

    override fun scheduleRepeating(context: Context, items: List<AlarmItem>) {
        if (!alarm.canScheduleAlarms(context)) return
        items.forEach { item ->
            alarm.alarmManager(context).setRepeating(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ item.timeMillis,
                /* intervalMillis = */ item.interval.interval * AlarmManager.INTERVAL_DAY,
                /* operation = */ alarmIntent(context, item)
            )
        }
    }

    override fun schedule(context: Context, items: List<AlarmItem>) {
        val alarmManager = alarm.alarmManager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) return
        items.forEach { item ->
            alarmManager.setExactAndAllowWhileIdle(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ item.timeMillis,
                /* operation = */ alarmIntent(context, item)
            )
        }
    }

    override fun cancel(context: Context, id: Long, times: Int) =
        (0 until times).forEach { index ->
            cancel(context, uriConverter.uri(id, index))
        }

    override fun cancel(context: Context, uri: Uri) {
        val pendingIntent = alarmIntent(context, uri)
        alarm.alarmManager(context).cancel(pendingIntent)
    }

    private fun alarmIntent(context: Context, item: AlarmItem): PendingIntent {
        val intent = intent(context, uriConverter.uri(item.uri))
        intent.putExtra(AlarmItem.KEY_ITEM, item)
        return alarm.alarmIntent(context, intent, item.id.toInt())
    }

    private fun alarmIntent(context: Context, uri: Uri): PendingIntent {
        val intent = intent(context, uri)
        val code = uriConverter.id(uri).toInt()
        return alarm.alarmIntent(context, intent, code)
    }

    private fun intent(context: Context, uri: Uri): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.setAction(AlarmReceiver.ACTION)
        intent.setData(uri)
        return intent
    }
}