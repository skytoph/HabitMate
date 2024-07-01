package com.github.skytoph.taski.core.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri

interface AlarmScheduler {
    fun scheduleRepeating(context: Context, items: List<AlarmItem>)
    fun schedule(context: Context, items: List<AlarmItem>)
    fun cancel(context: Context, uri: Uri)
    fun cancel(context: Context, id: Long, times: Int)

    class Base(
        private val alarm: AlarmProvider,
        private val uriConverter: HabitUriConverter,
    ) : AlarmScheduler {

        override fun scheduleRepeating(context: Context, items: List<AlarmItem>) =
            items.forEach { item ->
                alarm.alarmManager(context).setRepeating(
                    /* type = */ item.type,
                    /* triggerAtMillis = */ item.calendar.timeInMillis,
                    /* intervalMillis = */ item.interval,
                    /* operation = */ alarmIntent(context, item)
                )
            }

        override fun schedule(context: Context, items: List<AlarmItem>) =
            items.forEach { item ->
                alarm.alarmManager(context).setExact(
                    /* type = */ item.type,
                    /* triggerAtMillis = */ item.calendar.timeInMillis,
                    /* operation = */ alarmIntent(context, item)
                )
            }

        override fun cancel(context: Context, id: Long, times: Int) =
            (0 until times).forEach { index ->
                cancel(context, uriConverter.uri(id, index))
            }

        override fun cancel(context: Context, uri: Uri) {
            val intent = intent(context, uri)
            val flag = PendingIntent.FLAG_UPDATE_CURRENT
            alarm.alarmManager(context)
                .cancel(PendingIntent.getBroadcast(context, 0, intent, flag))
        }

        private fun alarmIntent(context: Context, item: AlarmItem): PendingIntent {
            val intent = intent(context, item.uri)
            intent.setData(item.uri)
            intent.putExtra(AlarmItem.KEY_ITEM, item)
            return alarm.alarmIntent(context, intent, item.id)
        }

        private fun intent(context: Context, uri: Uri): Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.setData(uri)
            return intent
        }
    }
}

interface ScheduleAlarm {
    fun schedule(scheduler: AlarmScheduler, context: Context, items: List<AlarmItem>)
}