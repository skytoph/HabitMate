package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
    fun interactor(datastore: IconsDatastore): IconsInteractor = IconsInteractor.Base(datastore)

    @Provides
    fun datastore(): IconsDatastore = IconsDatastore.Base()

    @Provides
    fun state(): MutableState<SelectIconState> = mutableStateOf(SelectIconState())
}