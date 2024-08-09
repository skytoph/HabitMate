package com.github.skytoph.taski.presentation.settings.restore

import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.list.component.DialogItem

data class RestoreState(
    val items: List<BackupItemUi>? = null,
    val isLoading: Boolean = true,
    val contextMenuItem: BackupItemUi? = null,
    val errorStateMessage: StringResource? = null,
    val dialog: RestoreDialogUi? = null,
    val permissionDialog: DialogItem? = null,
    val requestingPermission: Boolean = false
)

sealed class RestoreDialogUi {
    open val item: BackupItemUi? = null

    data class Delete(override val item: BackupItemUi) : RestoreDialogUi()
    data class Restore(override val item: BackupItemUi) : RestoreDialogUi()
    data object DeleteAllData : RestoreDialogUi()
    data object RequestPermissions : RestoreDialogUi()
}