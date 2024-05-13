package com.github.skytoph.taski.presentation.core.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.StringResource

data class AppBarState(
    val title: StringResource = StringResource.Value(""),
    val navigateUp: NavigateUp = NavigateUp(),
    val menuItems: List<AppBarAction> = emptyList(),
    val dropdownItems: List<AppBarAction> = emptyList(),
)

data class NavigateUp(
    val action: AppBarAction = AppBarAction.navigateUp,
    val canNavigateUp: Boolean = false
)

data class AppBarAction(
    val title: StringResource = StringResource.Value(""),
    val icon: ImageVector,
    val color: Color = Color.Black,
    val onClick: () -> Unit = {},
) {
    companion object {
        val navigateUp = AppBarAction(
            StringResource.ResId(R.string.action_navigate_up), Icons.Filled.ArrowBackIos,
        )

        val save = AppBarAction(
            StringResource.ResId(R.string.action_save_habit), Icons.Filled.Check
        )

        val edit = AppBarAction(StringResource.ResId(R.string.edit_habit), Icons.Filled.Edit)

        val delete =
            AppBarAction(StringResource.ResId(R.string.action_delete), Icons.Filled.Close)

        val add =
            AppBarAction(StringResource.ResId(R.string.action_add), Icons.Filled.Add)

        val view =
            AppBarAction(StringResource.ResId(R.string.action_view), Icons.Filled.FilterList)
    }
}