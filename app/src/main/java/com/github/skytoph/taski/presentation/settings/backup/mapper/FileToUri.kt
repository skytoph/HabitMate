package com.github.skytoph.taski.presentation.settings.backup.mapper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

interface FileToUri {
    fun readFromUri(contentResolver: ContentResolver, uri: Uri): ByteArray?
    fun writeToFile(byteArray: ByteArray, filename: String, context: Context): File
    fun getUriFromFile(file: File, context: Context): Uri?

    class Base : FileToUri {
        override fun readFromUri(contentResolver: ContentResolver, uri: Uri): ByteArray? =
            contentResolver.openInputStream(uri)?.run { readBytes() }

        override fun writeToFile(byteArray: ByteArray, filename: String, context: Context): File {
            val file = File(context.cacheDir, filename)
            file.writeBytes(byteArray)
            return file
        }

        override fun getUriFromFile(file: File, context: Context): Uri? =
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }
}