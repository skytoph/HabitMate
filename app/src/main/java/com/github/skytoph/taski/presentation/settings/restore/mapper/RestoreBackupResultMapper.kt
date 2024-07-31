package com.github.skytoph.taski.presentation.settings.restore.mapper

import android.content.Context
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.backup.BackupFile
import com.github.skytoph.taski.core.backup.BackupResult
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.NetworkErrorMapper
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages
import com.github.skytoph.taski.presentation.settings.restore.RestoreResultUi
import com.google.api.services.drive.model.File
import java.util.Locale

interface RestoreBackupResultMapper {
    fun map(result: BackupResult, locale: Locale? = null, context: Context? = null): RestoreResultUi

    class Base(private val mapper: BackupItemsUiMapper, private val networkMapper: NetworkErrorMapper) :
        RestoreBackupResultMapper {

        override fun map(result: BackupResult, locale: Locale?, context: Context?): RestoreResultUi = when (result) {
            is BackupResult.Success.ListOfFiles ->
                RestoreResultUi.Success.ListOfData(mapper.map(result.data.map { it.map() }, locale!!, context!!))

            is BackupResult.Success.FileDownloaded -> RestoreResultUi.Success.NextAction(result.file)

            is BackupResult.Success.Saved -> RestoreResultUi.Empty

            is BackupResult.Success.Deleted -> RestoreResultUi.Success.Deleted(
                message = BackupMessages.deleteItemSucceededMessage,
                time = result.time?.value,
                newData = mapper.map(result.newData.map { it.map() }, locale!!, context!!)
            )

            BackupResult.Success.FileRestored -> RestoreResultUi.Success.Message(BackupMessages.importSucceededMessage)

            is BackupResult.Success.AllFilesDeleted -> RestoreResultUi.Success.AllBackupsDeleted(BackupMessages.deleteDataSucceededMessage)

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
        }

        private fun File.map(): BackupFile = BackupFile(
            id = id,
            modifiedTime = modifiedTime.value,
            size = getSize()
        )
    }
}