package com.skytoph.taski.di.habit

import android.content.Context
import androidx.room.Room
import com.skytoph.taski.data.habit.database.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitDatabaseModule {

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): HabitDatabase =
        Room.databaseBuilder(context, HabitDatabase::class.java, "habits_db").build()
}