package com.github.skytoph.taski.di.habit

import android.content.Context
import androidx.room.Room
import com.github.skytoph.taski.data.habit.BaseHabitRepository
import com.github.skytoph.taski.data.habit.database.EntriesDao
import com.github.skytoph.taski.data.habit.database.HabitDao
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.domain.habit.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitRepositoryModule {

    @Provides
    @Singleton
    fun repository(habitsDao: HabitDao, entriesDao: EntriesDao): HabitRepository =
        BaseHabitRepository(habitDao = habitsDao, entryDao = entriesDao)

    @Provides
    @Singleton
    fun habitsDao(database: HabitDatabase): HabitDao = database.habitDao()

    @Provides
    @Singleton
    fun entriesDao(database: HabitDatabase): EntriesDao = database.entryDao()

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): HabitDatabase =
        Room.databaseBuilder(
            context, HabitDatabase::class.java, "habits_db"
        ).fallbackToDestructiveMigration() //todo remove
            .build()
}