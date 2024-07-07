package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InitAppBarModule {

    @Provides
    fun initAppBar(state: MutableState<AppBarState>): InitAppBar = InitAppBar.Base(state)
}