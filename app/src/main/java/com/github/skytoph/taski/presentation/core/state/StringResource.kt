package com.github.skytoph.taski.presentation.core.state

import android.content.Context
import androidx.annotation.StringRes

sealed class StringResource {
    abstract fun getString(context: Context): String

    class ResId(@StringRes private val id: Int) : StringResource() {
        override fun getString(context: Context): String = context.getString(id)
    }

    class Value(private val value: String) : StringResource() {
        override fun getString(context: Context): String = value
    }
}