package com.github.skytoph.taski.presentation.settings.restore.mapper

import android.content.Context
import android.text.format.Formatter
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.backup.BackupFile
import com.github.skytoph.taski.presentation.settings.restore.BackupItemUi
import java.text.SimpleDateFormat
import java.util.Locale

interface BackupItemsUiMapper {
    fun map(items: List<BackupFile>, locale: Locale, context: Context): List<BackupItemUi>

    class Base : BackupItemsUiMapper {
        override fun map(items: List<BackupFile>, locale: Locale, context: Context): List<BackupItemUi> =
            items.map { item ->
                BackupItemUi(
                    id = item.id,
                    date = SimpleDateFormat(context.getString(R.string.backup_date_format), locale)
                        .format(item.modifiedTime),
                    size = Formatter.formatShortFileSize(context, item.size)
                )
            }
    }
}