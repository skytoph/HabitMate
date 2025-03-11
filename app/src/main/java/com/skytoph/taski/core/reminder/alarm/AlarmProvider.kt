package com.skytoph.taski.core.reminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

interface AlarmProvider {
    fun alarmManager(context: Context): AlarmManager
    fun alarmIntent(context: Context, intent: Intent, code: Int): PendingIntent
    fun canScheduleAlarms(context: Context): Boolean

    class Base : AlarmProvider {

        override fun alarmManager(context: Context) =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        override fun alarmIntent(context: Context, intent: Intent, code: Int): PendingIntent =
            PendingIntent.getBroadcast(
                /* context = */ context,
                /* requestCode = */ code,
                /* intent = */ intent,
                /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        override fun canScheduleAlarms(context: Context): Boolean =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager(context).canScheduleExactAlarms()
    }
}