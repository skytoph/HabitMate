package com.skytoph.taski.presentation.settings.backup.mapper

import com.skytoph.taski.core.backup.BackupResult
import com.skytoph.taski.presentation.core.NetworkErrorMapper
import com.skytoph.taski.presentation.settings.backup.BackupResultUi

interface BackupResultMapper {
    fun map(result: BackupResult): BackupResultUi

    class Base(private val networkMapper: NetworkErrorMapper) : BackupResultMapper {
        override fun map(result: BackupResult): BackupResultUi = when {
            result is BackupResult.Fail && result.exception != null && networkMapper.isNetworkUnavailable(result.exception) -> BackupResultUi.NoConnection
            result is BackupResult.Fail.FileNotSaved -> BackupResultUi.BackupFailed
            result is BackupResult.Fail.FileNotDeleted -> BackupResultUi.DeletingAccount(false)
            result is BackupResult.Success.Saved -> BackupResultUi.Success.BackupSaved(result.time.value)
            result is BackupResult.Success.Deleted -> BackupResultUi.DeletingAccount(true)
            else -> BackupResultUi.Empty
        }
    }
}