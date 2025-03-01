package com.github.skytoph.taski.presentation.habit.icon

import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface IconsDatastore {
    fun unlockedFlow(profile: ProfileUi): Flow<Set<String>>
    suspend fun unlocked(profile: ProfileUi): Set<String>
    suspend fun unlock(profile: ProfileUi, icon: String)
    suspend fun unlock(profile: ProfileUi, icons: Set<String>)
    suspend fun delete(profile: ProfileUi)

    class Base(private val networkManager: NetworkManager) : IconsDatastore {
        override fun unlockedFlow(profile: ProfileUi): Flow<Set<String>> =
            document(profile)
            ?.snapshots()
            ?.map { snapshot ->
                snapshot[FIREBASE_FIELD]?.let {
                    if (it is List<*>) it.filterIsInstance<String>().toSet() else emptySet()
                } ?: emptySet()
            } ?: flowOf(emptySet())

        override suspend fun unlocked(profile: ProfileUi): Set<String> = document(profile)
            ?.get()
            ?.await()
            ?.get(FIREBASE_FIELD)
            .let { if (it is List<*>) it.filterIsInstance<String>().toSet() else emptySet() }

        override suspend fun unlock(profile: ProfileUi, icon: String) {
            document(profile)?.set(mapOf(FIREBASE_FIELD to FieldValue.arrayUnion(icon)), SetOptions.merge())
                ?.let { if (networkManager.isNetworkAvailable()) it.await() }
        }

        override suspend fun unlock(profile: ProfileUi, icons: Set<String>) {
            document(profile)
                ?.set(mapOf(FIREBASE_FIELD to FieldValue.arrayUnion(*icons.toTypedArray())), SetOptions.merge())
                ?.let { if (networkManager.isNetworkAvailable()) it.await() }
        }

        override suspend fun delete(profile: ProfileUi) {
            document(profile)?.delete()?.await()
        }

        private fun document(profile: ProfileUi): DocumentReference? =
            if (profile.isEmpty) null else Firebase.firestore.collection(FIREBASE_COLLECTION).document(profile.id)

        private companion object {
            const val FIREBASE_COLLECTION = "rewards"
            const val FIREBASE_FIELD = "icons"
        }
    }
}