package com.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.skytoph.taski.presentation.settings.reorder.ReorderState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ReorderHabitsModule {

    @Provides
    @ViewModelScoped
    fun state(): MutableState<ReorderState> = mutableStateOf(ReorderState())
}