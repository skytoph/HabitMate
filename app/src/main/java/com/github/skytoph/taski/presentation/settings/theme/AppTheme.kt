package com.github.skytoph.taski.presentation.settings.theme

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.StringResource

sealed interface AppTheme {
    val name: StringResource

    data object System : AppTheme {
        override val name: StringResource = StringResource.ResId(R.string.theme_system)
    }

    data object Light : AppTheme {
        override val name: StringResource = StringResource.ResId(R.string.theme_light)
    }

    data object Dark : AppTheme {
        override val name: StringResource = StringResource.ResId(R.string.theme_dark)
    }

    companion object {
        val values = listOf(System, Light, Dark)
    }
}