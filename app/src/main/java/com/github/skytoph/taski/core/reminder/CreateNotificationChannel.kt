package com.github.skytoph.taski.core.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

interface CreateNotificationChannel {
    fun createChannel(context: Context, channel: HabitNotificationChannel)

    class Base : CreateNotificationChannel {

        override fun createChannel(context: Context, channel: HabitNotificationChannel) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(channel.id, channel.name, channel.priority)
            )
        }
    }
}