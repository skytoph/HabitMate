package com.github.skytoph.taski.presentation.core.state

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup

sealed interface IconResource {
    fun id(context: Context): Int
    fun name(resources: Resources): String
    fun matches(icon: IconResource, context: Context): Boolean

    @Composable
    fun vector(context: Context): ImageVector

    class Id(@DrawableRes private val id: Int, private var name: String? = null) : IconResource {
        override fun id(context: Context): Int = id

        override fun name(resources: Resources): String =
            name ?: resources.getResourceEntryName(id).also { name = it }

        override fun matches(icon: IconResource, context: Context): Boolean =
            id == icon.id(context)

        @Composable
        override fun vector(context: Context): ImageVector = ImageVector.vectorResource(id)
    }

    class Name(private val name: String) : IconResource {
        override fun id(context: Context): Int =
            context.resources.getIdentifier(name, "drawable", context.packageName)

        override fun name(resources: Resources): String = name

        override fun matches(icon: IconResource, context: Context): Boolean =
            name == icon.name(context.resources)

        @Composable
        override fun vector(context: Context): ImageVector = ImageVector.vectorResource(id(context))
    }

    class Vector(private val vector: ImageVector) : IconResource {
        override fun id(context: Context): Int = -1

        override fun name(resources: Resources): String = vector.name.split(".")[1]

        override fun matches(icon: IconResource, context: Context): Boolean =
            vector.name == icon.name(context.resources)

        @Composable
        override fun vector(context: Context): ImageVector = vector
    }

    companion object {
        val Default = Id(IconsGroup.Default)
    }
}