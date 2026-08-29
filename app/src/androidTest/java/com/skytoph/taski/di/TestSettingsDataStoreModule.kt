@file:OptIn(ExperimentalCoroutinesApi::class)

package com.skytoph.taski.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.skytoph.taski.core.datastore.SettingsSerializer
import com.skytoph.taski.core.datastore.settings.Settings
import com.skytoph.taski.di.habit.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.io.File

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestSettingsDataStoreModule {

    @Provides
    fun settingsDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Settings> =
        DataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            serializer = SettingsSerializer,
            produceFile = {
                File.createTempFile("test_settings", ".json").apply {
                    deleteOnExit()
                }
            }
        )
}
