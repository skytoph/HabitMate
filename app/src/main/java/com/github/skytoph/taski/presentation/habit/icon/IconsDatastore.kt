package com.github.skytoph.taski.presentation.habit.icon

import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.auth.SignInWithGoogle
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
    fun unlockedFlow(): Flow<Set<String>>
    suspend fun unlocked(): Set<String>
    suspend fun unlock(icon: String)
    suspend fun unlock(icons: Set<String>)
    suspend fun delete()

    class Base(private val networkManager: NetworkManager) : IconsDatastore {
        override fun unlockedFlow(): Flow<Set<String>> = document()
            ?.snapshots()
            ?.map { snapshot ->
                snapshot[FIREBASE_FIELD]?.let {
                    if (it is List<*>) it.filterIsInstance<String>().toSet() else emptySet()
                } ?: emptySet()
            } ?: flowOf(emptySet())

        override suspend fun unlocked(): Set<String> = document()
            ?.get()
            ?.await()
            ?.get(FIREBASE_FIELD)
            .let { if (it is List<*>) it.filterIsInstance<String>().toSet() else emptySet() }

        override suspend fun unlock(icon: String) {
            document()?.set(mapOf(FIREBASE_FIELD to FieldValue.arrayUnion(icon)), SetOptions.merge())
                ?.let { if (networkManager.isNetworkAvailable()) it.await() }
        }

        override suspend fun unlock(icons: Set<String>) {
            document()?.set(mapOf(FIREBASE_FIELD to FieldValue.arrayUnion(*icons.toTypedArray())), SetOptions.merge())
                ?.let { if (networkManager.isNetworkAvailable()) it.await() }
        }

        override suspend fun delete() {
            document()?.delete()?.await()
        }

        private fun document(): DocumentReference? {
            val profile = SignInWithGoogle.DriveScope.profile()
            return if (profile.isEmpty) null
            else Firebase.firestore.collection(FIREBASE_COLLECTION).document(profile.id)
        }

        private companion object {
            const val FIREBASE_COLLECTION = "rewards"
            const val FIREBASE_FIELD = "icons"
        }
    }
}