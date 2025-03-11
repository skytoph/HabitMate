package com.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.skytoph.taski.core.backup.BackupDatastore
import com.skytoph.taski.core.backup.BackupManager
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.core.NetworkErrorMapper
import com.skytoph.taski.presentation.settings.restore.RestoreInteractor
import com.skytoph.taski.presentation.settings.restore.RestoreState
import com.skytoph.taski.presentation.settings.restore.mapper.BackupItemsUiMapper
import com.skytoph.taski.presentation.settings.restore.mapper.RestoreBackupResultMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RestoreViewModelModule {

    @Provides
    fun state(): MutableState<RestoreState> = mutableStateOf(RestoreState())

    @Provides
    fun interactor(repository: HabitRepository, datastore: BackupDatastore, mapper: RestoreBackupResultMapper, database: BackupManager, logger: Logger)
            : RestoreInteractor = RestoreInteractor.Base(repository, datastore, mapper, database, logger)

    @Provides
    fun mapper(mapper: BackupItemsUiMapper, networkMapper: NetworkErrorMapper): RestoreBackupResultMapper =
        RestoreBackupResultMapper.Base(mapper, networkMapper)

    @Provides
    fun networkMapper(): NetworkErrorMapper = NetworkErrorMapper.Base()

    @Provides
    fun mapperUi(): BackupItemsUiMapper = BackupItemsUiMapper.Base()
}