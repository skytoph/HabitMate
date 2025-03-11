package com.skytoph.taski.presentation.settings.backup.mapper

import android.content.Context
import com.skytoph.taski.R
import java.text.SimpleDateFormat
import java.util.Locale

object BackupFilename {
    fun generate(context: Context): String {
        val date = SimpleDateFormat(context.getString(R.string.backup_filename_date_format), Locale.US)
            .format(System.currentTimeMillis())
        return context.getString(R.string.backup_filename, date)
    }
}