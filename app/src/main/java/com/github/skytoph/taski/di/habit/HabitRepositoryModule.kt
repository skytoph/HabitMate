package com.github.skytoph.taski.di.habit

import android.content.Context
import androidx.room.Room
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.data.habit.database.EntriesDao
import com.github.skytoph.taski.data.habit.database.HabitDao
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.data.habit.mapper.EntryListMapper
import com.github.skytoph.taski.data.habit.mapper.HabitDBToDomainMapper
import com.github.skytoph.taski.data.habit.repository.BaseHabitRepository
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.details.mapper.CalculatorProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStateMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
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
    fun repository(
        habitsDao: HabitDao,
        entriesDao: EntriesDao,
        mapper: HabitDBToDomainMapper,
        entryMapper: EntryListMapper,
        stats: StatisticsUiMapper,
    ): HabitRepository = BaseHabitRepository(
        habitDao = habitsDao,
        entryDao = entriesDao,
        habitMapper = mapper,
        entryMapper = entryMapper,
        stats = stats
    )

    @Provides
    @Singleton
    fun habitsDao(database: HabitDatabase): HabitDao = database.habitDao()

    @Provides
    @Singleton
    fun entriesDao(database: HabitDatabase): EntriesDao = database.entryDao()

    @Provides
    @Singleton
    fun entryMapper(now: Now): EntryListMapper = EntryListMapper.Base(now)

    @Provides
    @Singleton
    fun habitDomainMapper(entryMapper: EntryListMapper): HabitDBToDomainMapper =
        HabitDBToDomainMapper.Base(entryMapper)

    @Provides
    @Singleton
    fun stateMapper(provider: CalculatorProvider): HabitStateMapper = HabitStateMapper(provider)

    @Provides
    @Singleton
    fun provider(): CalculatorProvider = CalculatorProvider.Base()

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): HabitDatabase =
        Room.databaseBuilder(
            context, HabitDatabase::class.java, "habits_db"
        ).fallbackToDestructiveMigration() //todo remove
            .build()
}