package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.BackupManager
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.core.Logger
import com.github.skytoph.taski.presentation.core.NetworkErrorMapper
import com.github.skytoph.taski.presentation.settings.restore.RestoreInteractor
import com.github.skytoph.taski.presentation.settings.restore.RestoreState
import com.github.skytoph.taski.presentation.settings.restore.mapper.BackupItemsUiMapper
import com.github.skytoph.taski.presentation.settings.restore.mapper.RestoreBackupResultMapper
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