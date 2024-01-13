package com.github.skytoph.taski.di.core

import com.github.skytoph.taski.presentation.core.ConvertIcon
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConvertIconModule {

    @Provides
    fun convertIcon(): ConvertIcon = ConvertIcon.Base()
}