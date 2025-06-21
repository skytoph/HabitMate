package com.skytoph.taski.di.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.skytoph.taski.core.crashlytics.Crashlytics
import com.skytoph.taski.core.datastore.CrashlyticsIdDataStore
import com.skytoph.taski.core.datastore.GenerateUserId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CrashlyticsModule {

    @Provides
    @Singleton
    fun crashlytics(idDatastore: CrashlyticsIdDataStore): Crashlytics =
        Crashlytics.Base(idDatastore)

    @Provides
    @Singleton
    fun idDataStore(datastore: DataStore<Preferences>, generateId: GenerateUserId): CrashlyticsIdDataStore =
        CrashlyticsIdDataStore.Base(datastore, generateId)

    @Provides
    fun generateId(): GenerateUserId = GenerateUserId.Base()

    @Provides
    fun preferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.preferencesDataStore

    val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
}