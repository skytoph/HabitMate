package com.github.skytoph.taski.app

import android.app.Application
import com.github.skytoph.taski.core.alarm.CreateNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HabitMateApplication : Application(),
    CreateNotificationChannel by CreateNotificationChannel.Base() {

    override fun onCreate() {
        super.onCreate()
        createChannel(context = applicationContext)
    }
}