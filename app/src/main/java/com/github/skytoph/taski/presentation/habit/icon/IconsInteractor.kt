@file:OptIn(ExperimentalCoroutinesApi::class)

package com.github.skytoph.taski.presentation.habit.icon

import android.content.Context
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.presentation.core.Logger
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.github.skytoph.taski.presentation.settings.backup.SignInInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

interface IconsInteractor : SignInInteractor<Boolean> {
    fun icons(context: Context): Flow<List<IconsLockedGroup>>
    suspend fun unlock(context: Context, icon: String): Boolean
    suspend fun shouldShowWarning(context: Context): Boolean

    class Base(
        private val datastore: IconsDatastore,
        private val auth: SignInWithGoogle,
        log: Logger,
        networkManager: NetworkManager,
        drive: BackupDatastore
    ) : IconsInteractor, SignInInteractor.Base<Boolean>(datastore, networkManager, drive, auth, log) {

        override fun icons(context: Context): Flow<List<IconsLockedGroup>> =
            auth.profileFlow(false)
                .flatMapLatest { datastore.unlockedFlow(it) }
                .map { icons ->
                    val unlocked = icons + DefaultIconsDatastore.unlocked(context.resources)
                    IconsGroup.allGroups.map { group ->
                        IconsLockedGroup(
                            titleResId = group.title,
                            icons = group.icons.map { icon ->
                                icon to unlocked.contains(IconResource.Id(icon).name(context.resources))
                            }
                        )
                    }
                }

        override suspend fun unlock(context: Context, icon: String): Boolean = try {
            if (profile().isEmpty) auth.signInAnonymously()
            datastore.unlock(auth.profile(), icon)
            true
        } catch (exception: Exception) {
            false
        }

        override fun mapResult(exception: Exception?, default: Boolean): Boolean = false

        override suspend fun mapResult(profile: ProfileUi, synchronized: Boolean?, permissionNeeded: Boolean): Boolean =
            true

        override suspend fun shouldShowWarning(context: Context): Boolean = !signedIn()

        private fun signedIn(): Boolean = profile().let { !it.isEmpty && !it.isAnonymous }

        private fun profile(): ProfileUi = auth.profile()

        override val defaultSignInFailResult: Boolean = false
        override val noConnectionResult: Boolean = false
        override val connectedResult: Boolean = true
        override val emptyResult: Boolean = true
    }
}