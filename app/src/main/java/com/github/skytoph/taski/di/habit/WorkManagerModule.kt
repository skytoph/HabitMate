package com.github.skytoph.taski.di.habit

import android.content.Context
import androidx.work.WorkManager
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.core.alarm.HabitUriConverter
import com.github.skytoph.taski.core.alarm.WorkScheduler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
        workManager: WorkManager, gson: Gson, uriConverter: HabitUriConverter
    ): AlarmScheduler = WorkScheduler(workManager, uriConverter, gson)

    @Provides
    @Singleton
    fun workManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    fun gson(): Gson = GsonBuilder().create()

    @Provides
    fun uriConverter(): HabitUriConverter = HabitUriConverter.Base()
}