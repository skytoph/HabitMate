package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.github.skytoph.taski.presentation.habit.icon.IconsInteractor
import com.github.skytoph.taski.presentation.habit.icon.SelectIconState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SelectIconInteractorModule {

    @Provides
    fun interactor(datastore: IconsDatastore, networkManager: NetworkManager): IconsInteractor =
        IconsInteractor.Base(datastore, networkManager)

    @Provides
    fun datastore(networkManager: NetworkManager): IconsDatastore = IconsDatastore.Base(networkManager)

    @Provides
    fun state(): MutableState<SelectIconState> = mutableStateOf(SelectIconState())
}