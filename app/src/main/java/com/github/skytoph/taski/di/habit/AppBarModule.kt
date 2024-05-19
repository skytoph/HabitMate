package com.github.skytoph.taski.di.habit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.component.AppBarState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppBarModule {

    @Provides
    @Singleton
    fun state(): MutableState<AppBarState> = mutableStateOf(AppBarState())

    @Provides
    @Singleton
    fun popupMessage(): PopupMessage.Mutable<SnackbarMessage, SnackbarHostState> =
        PopupMessage.Snackbar(SnackbarHostState())

    @Provides
    fun popupShow(popup: PopupMessage.Mutable<SnackbarMessage, SnackbarHostState>)
            : PopupMessage.Show<SnackbarMessage> = popup

    @Provides
    fun popupProvide(popup: PopupMessage.Mutable<SnackbarMessage, SnackbarHostState>)
            : PopupMessage.Provide<SnackbarHostState> = popup
}