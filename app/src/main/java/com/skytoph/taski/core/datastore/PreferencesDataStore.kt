package com.skytoph.taski.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

interface CrashlyticsIdDataStore {
    suspend fun id(): String
    suspend fun saveId(value: String)

    class Base(
        private val datastore: DataStore<Preferences>,
        private val generateId: GenerateUserId
    ) : CrashlyticsIdDataStore {

        private val idPrefKey = stringPreferencesKey(KEY_UID)

        override suspend fun id(): String {
            val storedId = datastore.data.map { preferences -> preferences[idPrefKey] }.first()
            val id = storedId ?: generateId.id().also { saveId(it) }
            return id
        }

        override suspend fun saveId(value: String) {
            datastore.edit { settings ->
                settings[idPrefKey] = value
            }
        }

        private companion object {
            const val KEY_UID = "key_crashlytics_user_id"
        }
    }
}

interface GenerateUserId {
    fun id(): String

    class Base : GenerateUserId {
        override fun id(): String {
            val id = UUID.randomUUID().toString()
            return id
        }
    }
}