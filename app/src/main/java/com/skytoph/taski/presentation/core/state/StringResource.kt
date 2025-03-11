package com.skytoph.taski.presentation.core.state

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import java.io.Serializable

sealed class StringResource : Serializable {
    abstract fun getString(context: Context): String

    class ResId(@StringRes private val id: Int) : StringResource() {
        override fun getString(context: Context): String = context.getString(id)
    }

    class Value(private val value: String) : StringResource() {
        override fun getString(context: Context): String = value
    }

    class Identifier(private val name: String) : StringResource() {
        override fun getString(context: Context): String =
            context.resources.getString(context.resources.getIdentifier(name, "string", context.packageName))
    }

    class IdentifierFromId(@StringRes private val id: Int) {
        fun getIdentifier(context: Context): String = context.resources.getResourceEntryName(id)
    }
}

fun Context.resourceUri(resourceId: Int): Uri = with(resources) {
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}