package com.github.skytoph.taski.core.alarm

import android.content.Context
import android.content.Intent

interface AlarmScheduler {
    fun schedule(context: Context, item: List<AlarmItem>)

    class Base(private val alarm: AlarmProvider) : AlarmScheduler {
        override fun schedule(context: Context, items: List<AlarmItem>) = items.forEach { item ->
            val intent = Intent(context, AlarmReceiver::class.java)
            item.putToIntent(context, intent)
            val alarmIntent = alarm.alarmIntent(context, intent)

//            alarm.alarmManager(context).setRepeating(
//                /* type = */ item.type,
//                /* triggerAtMillis = */ item.calendar.timeInMillis,
//                /* intervalMillis = */ item.interval,
//                /* operation = */ alarmIntent
//            )
        }
    }
}