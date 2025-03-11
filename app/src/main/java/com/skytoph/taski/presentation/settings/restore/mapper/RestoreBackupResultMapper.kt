package com.skytoph.taski.presentation.settings.restore.mapper

import android.content.Context
import com.google.api.services.drive.model.File
import com.skytoph.taski.R
import com.skytoph.taski.core.backup.BackupFile
import com.skytoph.taski.core.backup.BackupResult
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.NetworkErrorMapper
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.settings.backup.BackupMessages
import com.skytoph.taski.presentation.settings.restore.RestoreResultUi
import java.util.Locale

interface RestoreBackupResultMapper {
    fun map(result: BackupResult, is24HoursFormat: Boolean, locale: Locale? = null, context: Context? = null)
            : RestoreResultUi

    class Base(private val mapper: BackupItemsUiMapper, private val networkMapper: NetworkErrorMapper) :
        RestoreBackupResultMapper {

        override fun map(result: BackupResult, is24HoursFormat: Boolean, locale: Locale?, context: Context?)
                : RestoreResultUi = when (result) {
            is BackupResult.Success.ListOfFiles -> RestoreResultUi.Success.ListOfData(
                mapper.map(result.data.map { it.map() }, locale!!, context!!, is24HoursFormat)
            )

            is BackupResult.Success.FileDownloaded ->
                RestoreResultUi.Success.NextAction(result.file, result.restoreSettings, is24HoursFormat, context!!)

            is BackupResult.Success.Deleted -> RestoreResultUi.Success.Deleted(
                message = BackupMessages.deleteItemSucceededMessage,
                time = result.time?.value,
                newData = mapper.map(result.newData.map { it.map() }, locale!!, context!!, is24HoursFormat)
            )

            is BackupResult.Success.FileRestored -> RestoreResultUi.Success.Restored(
                containsReminders = result.containsReminders,
                needsPermission = result.needsPermission,
                message = BackupMessages.importSucceededMessage
            )

            BackupResult.Success.AllFilesDeleted -> RestoreResultUi.Success.AllBackupsDeleted(BackupMessages.deleteDataSucceededMessage)

            is BackupResult.Fail -> when {
                result.exception?.let { networkMapper.isNetworkUnavailable(it) } ?: false ->
                    RestoreResultUi.ErrorState(StringResource.ResId(R.string.restore_no_connection))

                result is BackupResult.Fail.DriveIsNotConnected ->
                    RestoreResultUi.ErrorState(StringResource.ResId(R.string.drive_unavailable))

                else -> RestoreResultUi.Message(
                    message = SnackbarMessage(
                        title = StringResource.ResId(R.string.fail_title),
                        icon = IconResource.Id(R.drawable.folder_x),
                        messageResource = when (result) {
                            is BackupResult.Fail.FileNotSaved -> StringResource.ResId(R.string.error_saving_backup)
                            is BackupResult.Fail.FileNotDownloaded -> StringResource.ResId(R.string.error_downloading_backup)
                            is BackupResult.Fail.FileNotDeleted -> StringResource.ResId(R.string.error_deleting_backup)
                            else -> StringResource.ResId(R.string.error_general)
                        }
                    )
                )
            }

            else -> RestoreResultUi.Empty
        }

        private fun File.map(): BackupFile = BackupFile(
            id = id,
            modifiedTime = modifiedTime.value,
            size = getSize()
        )
    }
}