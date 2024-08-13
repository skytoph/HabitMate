package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.BackupManager
import com.github.skytoph.taski.core.backup.StringCompressor
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.domain.habit.HabitRepository
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
    fun databaseExporter(database: HabitDatabase, gson: Gson, compressor: StringCompressor, settings: SettingsCache)
            : BackupManager = BackupManager.Base(database, gson, compressor, settings)

    @Provides
    fun compressor(): StringCompressor = StringCompressor.Base()

    @Provides
    fun writeFile(): FileToUri = FileToUri.Base()

    @Provides
    fun mapper(networkMapper: NetworkErrorMapper): BackupResultMapper = BackupResultMapper.Base(networkMapper)

    @Provides
    fun interactor(
        backup: BackupManager,
        datastore: BackupDatastore,
        fileWriter: FileToUri,
        mapper: BackupResultMapper,
        networkMapper: NetworkErrorMapper,
        repository: HabitRepository
    ): BackupInteractor = BackupInteractor.Base(repository, backup, datastore, fileWriter, mapper, networkMapper)
}