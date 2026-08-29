package com.skytoph.taski.mock.datastore

import com.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.skytoph.taski.presentation.settings.backup.ProfileUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestIconsDatastore : IconsDatastore {

    private val data: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())

    override fun unlockedFlow(profile: ProfileUi): Flow<Set<String>> = data

    override suspend fun unlocked(profile: ProfileUi): Set<String> = data.value

    override suspend fun unlock(profile: ProfileUi, icon: String) {
        data.value = data.value + icon
    }

    override suspend fun unlock(profile: ProfileUi, icons: Set<String>) {
        data.value = data.value + icons
    }

    override suspend fun delete(profile: ProfileUi) {
        data.value = emptySet()
    }
}