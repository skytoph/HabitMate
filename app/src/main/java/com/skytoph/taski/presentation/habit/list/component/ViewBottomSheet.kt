@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.core.datastore.settings.FilterHabits
import com.skytoph.taski.core.datastore.settings.HabitsView
import com.skytoph.taski.core.datastore.settings.SortHabits
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.presentation.core.component.OptionsDropdown
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ViewBottomSheet(
    state: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    view: HabitsView = HabitsView(),
    hideBottomSheet: () -> Unit = {},
    selectViewType: (ViewType) -> Unit = {},
    selectSorting: (SortHabits) -> Unit = {},
    selectFilter: (FilterHabits) -> Unit = {},
    reorder: () -> Unit = {},
    showTodayHabitsOnly: (Boolean) -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.widthIn(max = 640.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OptionsDropdown(
                title = stringResource(R.string.view_layout),
                options = ViewType.options,
                selected = view.viewType,
                selectOption = selectViewType
            )
            HorizontalDivider()
            OptionsDropdown(
                title = stringResource(R.string.view_sort),
                options = SortHabits.options,
                selected = view.sortBy,
                selectOption = selectSorting
            )
            HorizontalDivider()
            OptionsDropdown(
                title = stringResource(R.string.view_filter),
                options = FilterHabits.options,
                selected = view.filterBy,
                selectOption = selectFilter
            )
            HorizontalDivider()
            OptionSwitch(
                text = stringResource(R.string.show_only_todays_habits),
                isChecked = view.showTodayHabitsOnly,
                icon = Icons.Default.Check,
                onClick = showTodayHabitsOnly
            )
            HorizontalDivider()
            ButtonWithIcon(
                onClick = reorder,
                title = stringResource(R.string.reorder_habits),
                icon = ImageVector.vectorResource(R.drawable.arrow_up_down),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit = {},
    title: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
private fun DarkBottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        ViewBottomSheet(state = rememberStandardBottomSheetState())
    }
}