package com.github.skytoph.taski.presentation.core.state

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.github.skytoph.taski.R

interface IconResource {
    fun id(context: Context): Int
    fun name(resources: Resources): String
    fun matches(icon: IconResource, context: Context): Boolean

    class Id(@DrawableRes private val id: Int) : IconResource {
        override fun id(context: Context): Int = id

        override fun name(resources: Resources): String = resources.getResourceEntryName(id)

        override fun matches(icon: IconResource, context: Context): Boolean =
            id == icon.id(context)
    }

    class Name(private val name: String) : IconResource {
        override fun id(context: Context): Int =
            context.resources.getIdentifier(name, "drawable", context.packageName)

        override fun name(resources: Resources): String = name

        override fun matches(icon: IconResource, context: Context): Boolean =
            name == icon.name(context.resources)
    }

    companion object {
        val Default = Id(R.drawable.activity)
    }
}