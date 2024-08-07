package com.github.skytoph.taski.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.github.skytoph.taski.core.reminder.CreateNotificationChannel
import com.github.skytoph.taski.core.reminder.HabitNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class HabitMateApplication : Application(),
    CreateNotificationChannel by CreateNotificationChannel.Base() , Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createChannel(context = applicationContext, channel = HabitNotificationChannel.HabitReminder)
    }
}