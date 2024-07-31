package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.DatabaseBackup
import com.github.skytoph.taski.core.backup.StringCompressor
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.presentation.core.NetworkErrorMapper
import com.github.skytoph.taski.presentation.settings.backup.BackupInteractor
import com.github.skytoph.taski.presentation.settings.backup.mapper.BackupResultMapper
import com.github.skytoph.taski.presentation.settings.backup.mapper.FileToUri
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BackupModule {

    @Provides
    fun databaseExporter(database: HabitDatabase, gson: Gson, compressor: StringCompressor): DatabaseBackup =
        DatabaseBackup.Base(database, gson, compressor)

    @Provides
    fun compressor(): StringCompressor = StringCompressor.Base()

    @Provides
    fun writeFile(): FileToUri = FileToUri.Base()

    @Provides
    fun mapper(networkMapper: NetworkErrorMapper): BackupResultMapper = BackupResultMapper.Base(networkMapper)

    @Provides
    fun interactor(
        backup: DatabaseBackup, datastore: BackupDatastore, fileWriter: FileToUri, mapper: BackupResultMapper
    ): BackupInteractor = BackupInteractor.Base(backup, datastore, fileWriter, mapper)
}