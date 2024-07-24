package com.github.skytoph.taski.di.habit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.skytoph.taski.core.datastore.InitializeEmptyValues
import com.github.skytoph.taski.core.datastore.Settings
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.core.datastore.SettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun dataStore(@ApplicationContext context: Context): DataStore<Settings> = context.settingsDataStore

    @Provides
    @Singleton
    fun settings(dataStore: DataStore<Settings>): SettingsCache = SettingsCache.Base(dataStore = dataStore)

    @Provides
    fun mapper(): InitializeEmptyValues = InitializeEmptyValues()
}

val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = SettingsSerializer.FILENAME,
    serializer = SettingsSerializer
)