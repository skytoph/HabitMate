package com.github.skytoph.taski.core.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

interface AlarmScheduler {
    fun scheduleRepeating(context: Context, items: List<AlarmItem>)
    fun schedule(context: Context, items: List<AlarmItem>)
    fun cancel(context: Context, uri: Uri)
    fun cancel(context: Context, id: Long, times: Int)

    class Base(
        private val alarm: AlarmProvider,
        private val uriConverter: HabitUriConverter,
    ) : AlarmScheduler {

        override fun scheduleRepeating(context: Context, items: List<AlarmItem>) {
            val alarmManager = alarm.alarmManager(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) return
            items.forEach { item ->
                alarmManager.setRepeating(
                    /* type = */ item.type,
                    /* triggerAtMillis = */ item.calendar.timeInMillis,
                    /* intervalMillis = */ item.interval,
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
                    /* type = */ item.type,
                    /* triggerAtMillis = */ item.calendar.timeInMillis,
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
            val intent = intent(context, item.uri)
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
}

interface ScheduleAlarm {
    fun schedule(scheduler: AlarmScheduler, context: Context, items: List<AlarmItem>)
}