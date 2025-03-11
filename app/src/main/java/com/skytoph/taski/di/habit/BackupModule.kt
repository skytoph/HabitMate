package com.skytoph.taski.di.habit

import com.google.gson.Gson
import com.skytoph.taski.core.NetworkManager
import com.skytoph.taski.core.auth.SignInWithGoogle
import com.skytoph.taski.core.backup.BackupDatastore
import com.skytoph.taski.core.backup.BackupManager
import com.skytoph.taski.core.backup.StringCompressor
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.data.habit.database.HabitDatabase
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.core.NetworkErrorMapper
import com.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.skytoph.taski.presentation.settings.backup.BackupInteractor
import com.skytoph.taski.presentation.settings.backup.mapper.BackupResultMapper
import com.skytoph.taski.presentation.settings.backup.mapper.FileToUri
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
        repository: HabitRepository,
        icons: IconsDatastore,
        networkManager: NetworkManager,
        log: Logger,
        auth: SignInWithGoogle
    ): BackupInteractor = BackupInteractor.Base(
        repository, backup, datastore, fileWriter, mapper, icons, networkMapper, log, auth, networkManager
    )
}