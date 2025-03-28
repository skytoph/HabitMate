package com.skytoph.taski.di.habit

import android.content.Context
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skytoph.taski.core.adapter.GeneralTypeAdapterFactory
import com.skytoph.taski.core.reminder.HabitUriConverter
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.core.reminder.alarm.AlarmProvider
import com.skytoph.taski.core.reminder.alarm.AlarmScheduler
import com.skytoph.taski.presentation.core.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun scheduler(
        alarmProvider: AlarmProvider,
        uriConverter: HabitUriConverter,
        gson: Gson,
        @ApplicationContext context: Context,
        log: Logger
    ): ReminderScheduler = AlarmScheduler(alarmProvider, uriConverter, gson, context, log)

//    @Provides
//    @Singleton
//    fun scheduler(
//        workManager: WorkManager, gson: Gson, uriConverter: HabitUriConverter
//    ): ReminderScheduler = WorkScheduler(workManager, uriConverter, gson)

    @Provides
    @Singleton
    fun workManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    fun gson(): Gson = GsonBuilder().registerTypeAdapterFactory(GeneralTypeAdapterFactory()).create()

    @Provides
    fun uriConverter(): HabitUriConverter = HabitUriConverter.Base()
}