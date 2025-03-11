package com.skytoph.taski.presentation.core.component

import androidx.compose.ui.graphics.Color
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

data class AppBarState(
    val title: StringResource = StringResource.Value(""),
    val navigateUp: NavigateUp = NavigateUp(),
    val menuItems: List<AppBarAction> = emptyList(),
    val dropdownItems: List<AppBarAction> = emptyList(),
    val isListExpanded: Boolean = false
)

data class NavigateUp(
    val action: AppBarAction = AppBarAction.navigateUp,
    val canNavigateUp: Boolean = false
)

data class AppBarAction(
    val title: StringResource = StringResource.Value(""),
    val icon: IconResource? = null,
    val color: Color = Color.Black,
    val onClick: () -> Unit = {},
    val checked: Boolean? = null
) {
    companion object {
        val navigateUp =
            AppBarAction(StringResource.ResId(R.string.action_navigate_up), IconResource.Id(R.drawable.chevron_left))

        val save = AppBarAction(StringResource.ResId(R.string.action_save_habit), IconResource.Id(R.drawable.check_thin))

        val edit = AppBarAction(StringResource.ResId(R.string.edit_habit), IconResource.Id(R.drawable.pencil))

        val delete = AppBarAction(StringResource.ResId(R.string.action_delete), IconResource.Id(R.drawable.trash))

        val add = AppBarAction(StringResource.ResId(R.string.action_add), IconResource.Id(R.drawable.plus))

        val view =
            AppBarAction(StringResource.ResId(R.string.action_view), IconResource.Id(R.drawable.sliders_horizontal))

        val settings =
            AppBarAction(StringResource.ResId(R.string.action_settings), IconResource.Id(R.drawable.settings))
    }
}