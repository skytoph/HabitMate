package com.github.skytoph.taski.presentation.core.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

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
    val icon: IconResource,
    val color: Color = Color.Black,
    val onClick: () -> Unit = {},
) {
    companion object {
        val navigateUp = AppBarAction(
            StringResource.ResId(R.string.action_navigate_up), IconResource.Vector(Icons.Filled.ArrowBackIos),
        )

        val save = AppBarAction(
            StringResource.ResId(R.string.action_save_habit), IconResource.Vector(Icons.Filled.Check)
        )

        val edit = AppBarAction(StringResource.ResId(R.string.edit_habit), IconResource.Id(R.drawable.pencil))

        val delete =
            AppBarAction(StringResource.ResId(R.string.action_delete), IconResource.Id(R.drawable.trash))

        val add =
            AppBarAction(StringResource.ResId(R.string.action_add), IconResource.Vector(Icons.Filled.Add))

        val view =
            AppBarAction(StringResource.ResId(R.string.action_view), IconResource.Id(R.drawable.sliders_horizontal))

        val settings =
            AppBarAction(StringResource.ResId(R.string.action_settings), IconResource.Id(R.drawable.settings))
    }
}