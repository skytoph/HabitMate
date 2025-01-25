package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.presentation.habit.icon.IconsDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IconsDatastoreModule {

    @Provides
    @Singleton
    fun datastore(networkManager: NetworkManager): IconsDatastore = IconsDatastore.Base(networkManager)
}