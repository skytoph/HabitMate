package com.github.skytoph.taski.presentation.settings.restore.mapper

import android.content.Context
import android.text.format.Formatter
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.backup.BackupFile
import com.github.skytoph.taski.presentation.settings.restore.BackupItemUi
import java.text.SimpleDateFormat
import java.util.Locale

interface BackupItemsUiMapper {
    fun map(items: List<BackupFile>, locale: Locale, context: Context, is24HoursFormat: Boolean): List<BackupItemUi>

    class Base : BackupItemsUiMapper {
        override fun map(items: List<BackupFile>, locale: Locale, context: Context, is24HoursFormat: Boolean)
                : List<BackupItemUi> =
            items.map { item ->
                val date = context.getString(
                    if (is24HoursFormat) R.string.backup_date_format_24h_format
                    else R.string.backup_date_format_12h_format
                )
                BackupItemUi(
                    id = item.id,
                    date = SimpleDateFormat(date, locale).format(item.modifiedTime),
                    size = Formatter.formatShortFileSize(context, item.size)
                )
            }
    }
}