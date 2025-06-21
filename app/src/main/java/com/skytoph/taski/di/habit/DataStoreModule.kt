package com.skytoph.taski.di.habit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.skytoph.taski.core.datastore.SettingsSerializer
import com.skytoph.taski.core.datastore.settings.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun settingsDataStore(@ApplicationContext context: Context): DataStore<Settings> = context.settingsDataStore

    val Context.settingsDataStore: DataStore<Settings> by dataStore(
        fileName = SETTINGS_FILENAME,
        serializer = SettingsSerializer
    )

    private const val SETTINGS_FILENAME = "settings.json"
}
