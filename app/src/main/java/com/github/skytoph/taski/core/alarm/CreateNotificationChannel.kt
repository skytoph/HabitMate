package com.github.skytoph.taski.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

interface CreateNotificationChannel {
    fun createChannel(context: Context)

    class Base : CreateNotificationChannel {
        override fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "alarm_id"
                val channelName = "notifications"
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )

                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}