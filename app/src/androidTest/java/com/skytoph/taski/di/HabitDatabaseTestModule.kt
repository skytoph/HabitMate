package com.skytoph.taski.di

import android.content.Context
import androidx.room.Room
import com.skytoph.taski.data.habit.database.HabitDatabase
import com.skytoph.taski.di.habit.HabitDatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HabitDatabaseModule::class]
)
class HabitDatabaseTestModule {

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): HabitDatabase =
        Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java).build()
}