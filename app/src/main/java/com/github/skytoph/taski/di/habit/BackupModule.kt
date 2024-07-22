package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.backup.DatabaseExporter
import com.github.skytoph.taski.core.backup.StringCompressor
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.presentation.settings.backup.BackupInteractor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BackupModule {

    @Provides
    fun databaseExporter(database: HabitDatabase, gson: Gson, compressor: StringCompressor): DatabaseExporter =
        DatabaseExporter(database, gson, compressor)

    @Provides
    fun compressor(): StringCompressor = StringCompressor.Base()

    @Provides
    fun interactor(backup: DatabaseExporter): BackupInteractor = BackupInteractor.Base(backup)
}