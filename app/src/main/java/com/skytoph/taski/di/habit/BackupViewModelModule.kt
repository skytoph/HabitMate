package com.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.skytoph.taski.presentation.settings.backup.BackupState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BackupViewModelModule {

    @Provides
    fun state(): MutableState<BackupState> = mutableStateOf(BackupState())
}