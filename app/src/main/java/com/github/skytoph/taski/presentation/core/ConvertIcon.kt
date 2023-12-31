package com.github.skytoph.taski.presentation.core

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

interface ConvertIcon {
    fun getIconName(icon: ImageVector): String

    fun filledIconByName(name: String): ImageVector

    class Base : ConvertIcon {

        override fun getIconName(icon: ImageVector): String {
            return icon.name.split(".")[1]
        }

        override fun filledIconByName(name: String): ImageVector {
            val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
            val method = cl.declaredMethods.first()
            return method.invoke(null, Icons.Filled) as ImageVector
        }
    }
}