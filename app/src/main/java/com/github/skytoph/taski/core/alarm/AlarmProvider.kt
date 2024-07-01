package com.github.skytoph.taski.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

interface AlarmProvider {
    fun alarmManager(context: Context): AlarmManager
    fun alarmIntent(context: Context, intent: Intent, code: Int): PendingIntent

    class Base : AlarmProvider {

        override fun alarmManager(context: Context) =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        override fun alarmIntent(context: Context, intent: Intent, code: Int): PendingIntent =
            PendingIntent.getBroadcast(
                /* context = */ context,
                /* requestCode = */ code,
                /* intent = */ intent,
                /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT
            )
    }
}