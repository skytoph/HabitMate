package com.skytoph.taski.presentation.appbar

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import com.skytoph.taski.presentation.core.component.AppBarAction
import com.skytoph.taski.presentation.core.component.AppBarState
import com.skytoph.taski.presentation.core.state.StringResource

interface InitAppBar {
    fun initAppBar(
        @StringRes title: Int? = null,
        canNavigateUp: Boolean = true,
        menuItems: List<AppBarAction> = emptyList(),
        dropDownItems: List<AppBarAction> = emptyList()
    )

    class Base(private val state: MutableState<AppBarState>) : InitAppBar {
        override fun initAppBar(
            title: Int?,
            canNavigateUp: Boolean,
            menuItems: List<AppBarAction>,
            dropdownItems: List<AppBarAction>
        ) {
            AppBarEvent.Update(
                title = title?.let { StringResource.ResId(it) } ?: StringResource.Value(""),
                menuItems = menuItems,
                dropdownItems = dropdownItems,
                canNavigateUp = canNavigateUp
            ).handle(state)
        }
    }
}