package com.skytoph.taski.di.habit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.datastore.SettingsSerializer
import com.skytoph.taski.core.datastore.settings.InitializeEmptyValues
import com.skytoph.taski.core.datastore.settings.Settings
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
    fun settingsDataStore(@ApplicationContext context: Context): DataStore<Settings> = context.settingsDataStore

    @Provides
    @Singleton
    fun settings(dataStore: DataStore<Settings>): SettingsCache = SettingsCache.Base(dataStore = dataStore)

    @Provides
    fun preferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.preferencesDataStore

    @Provides
    fun mapper(@ApplicationContext context: Context): InitializeEmptyValues = InitializeEmptyValues(context)
}

val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = SettingsSerializer.FILENAME,
    serializer = SettingsSerializer
)

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")