package com.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.skytoph.taski.core.NetworkManager
import com.skytoph.taski.core.auth.SignInWithGoogle
import com.skytoph.taski.core.backup.BackupDatastore
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.skytoph.taski.presentation.habit.icon.IconsInteractor
import com.skytoph.taski.presentation.habit.icon.SelectIconState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SelectIconInteractorModule {

    @Provides
    fun interactor(
        datastore: IconsDatastore, networkManager: NetworkManager, drive: BackupDatastore,
        auth: SignInWithGoogle, log: Logger
    ): IconsInteractor = IconsInteractor.Base(datastore, auth, log, networkManager, drive)

    @Provides
    fun state(): MutableState<SelectIconState> = mutableStateOf(SelectIconState())
}