package com.github.skytoph.taski.presentation.habit.icon

import android.content.res.Resources
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.github.skytoph.taski.presentation.settings.backup.SignInInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IconsInteractor : SignInInteractor<Boolean> {
    fun icons(resources: Resources): Flow<List<IconsLockedGroup>>
    suspend fun unlock(icon: String): Boolean
    suspend fun shouldShowWarning(): Boolean

    class Base(private val datastore: IconsDatastore, networkManager: NetworkManager, drive: BackupDatastore) :
        IconsInteractor, SignInInteractor.Base<Boolean>(datastore, networkManager, drive) {

        override fun icons(resources: Resources): Flow<List<IconsLockedGroup>> =
            datastore.unlockedFlow().map { icons ->
                val unlocked = icons + DefaultIconsDatastore.unlocked(resources)
                IconsGroup.allGroups.map { group ->
                    IconsLockedGroup(
                        titleResId = group.title,
                        icons = group.icons.map { icon ->
                            icon to unlocked.contains(IconResource.Id(icon).name(resources))
                        }
                    )
                }
            }

        override suspend fun unlock(icon: String): Boolean = try {
            datastore.unlock(icon)
            true
        } catch (exception: Exception) {
            false
        }

        override fun mapResult(exception: Exception?, default: Boolean): Boolean = false

        override suspend fun mapResult(profile: ProfileUi, synchronized: Boolean?): Boolean = true

        override suspend fun shouldShowWarning(): Boolean = !signedIn()

        private fun signedIn(): Boolean = SignInWithGoogle.DriveScope.profile().let { it != null && !it.isAnonymous }

        override val defaultSigningInResult: Boolean = false
        override val noConnectionResult: Boolean = false
        override val connectedResult: Boolean = true
    }
}