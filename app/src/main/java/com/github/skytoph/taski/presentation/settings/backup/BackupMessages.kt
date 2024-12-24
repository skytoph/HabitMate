package com.github.skytoph.taski.presentation.settings.backup

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

object BackupMessages {
    val deletingAccountFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_deleting_account),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.circle_user)
    )
    val deletingAccountSucceededMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.success_deleting_account),
        title = StringResource.ResId(R.string.success_title),
        icon = IconResource.Id(id = R.drawable.circle_user)
    )
    val clearingDataFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_clearing_data),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.trash)
    )
    val clearingDataSucceededMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.success_clearing_data),
        title = StringResource.ResId(R.string.success_title),
        icon = IconResource.Id(id = R.drawable.trash)
    )
    val signInFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.failed_to_sign_in_message),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.circle_user)
    )
    val signOutFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.failed_to_sign_out),
        title = StringResource.ResId(R.string.fail_title),
        icon = IconResource.Id(id = R.drawable.circle_user)
    )
    val syncFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_sync),
        title = StringResource.ResId(R.string.sync_title),
        icon = IconResource.Id(id = R.drawable.folder_x)
    )
    val exportFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_export),
        title = StringResource.ResId(R.string.error_export_title),
        icon = IconResource.Id(id = R.drawable.folder_x)
    )
    val importFailedMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.error_import),
        title = StringResource.ResId(R.string.import_error_title),
        icon = IconResource.Id(id = R.drawable.folder_x)
    )
    val importSucceededMessage = SnackbarMessage(
        messageResource = StringResource.ResId(R.string.success_import_message),
        title = StringResource.ResId(R.string.success_import_title),
        icon = IconResource.Id(id = R.drawable.folder_input)
    )
    val deleteItemSucceededMessage = SnackbarMessage(
        title = StringResource.ResId(R.string.success_title),
        messageResource = StringResource.ResId(R.string.success_delete_backup),
        icon = IconResource.Id(R.drawable.refresh_cw)
    )
    val deleteDataSucceededMessage = SnackbarMessage(
        title = StringResource.ResId(R.string.success_title),
        messageResource = StringResource.ResId(R.string.success_delete_backups),
        icon = IconResource.Id(R.drawable.folder_sync)
    )
    val iconsSynchronizeSuccessMessage = SnackbarMessage(
        title = StringResource.ResId(R.string.success_title),
        messageResource = StringResource.ResId(R.string.success_synchronizing_icons),
        icon = IconResource.Id(R.drawable.sparkle)
    )
    val iconsSynchronizeErrorMessage = SnackbarMessage(
        title = StringResource.ResId(R.string.fail_title),
        messageResource = StringResource.ResId(R.string.error_synchronizing_icons),
        icon = IconResource.Id(R.drawable.sparkle)
    )
}