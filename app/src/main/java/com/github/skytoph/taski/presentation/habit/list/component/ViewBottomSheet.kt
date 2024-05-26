@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.OptionsDropdown
import com.github.skytoph.taski.presentation.habit.list.view.FilterOption
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView
import com.github.skytoph.taski.presentation.habit.list.view.HabitsViewOptionsProvider
import com.github.skytoph.taski.presentation.habit.list.view.SortOption
import com.github.skytoph.taski.presentation.habit.list.view.ViewOption
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ViewBottomSheet(
    state: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    view: HabitsView = HabitsView(),
    hideBottomSheet: () -> Unit = {},
    selectViewType: (ViewOption) -> Unit = {},
    selectSorting: (SortOption) -> Unit = {},
    selectFilter: (FilterOption) -> Unit = {},
    reorder: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OptionsDropdown(
                title = stringResource(R.string.view_layout),
                options = HabitsViewOptionsProvider.viewOptions,
                selected = view.viewType,
                selectOption = selectViewType
            )
            Divider()
            OptionsDropdown(
                title = stringResource(R.string.view_filter),
                options = HabitsViewOptionsProvider.filterOptions,
                selected = view.filterBy,
                selectOption = selectFilter
            )
            Divider()
            OptionsDropdown(
                title = stringResource(R.string.view_sort),
                options = HabitsViewOptionsProvider.sortOptions,
                selected = view.sortBy,
                selectOption = selectSorting
            )
            Divider()
            ReorderButton(reorder)
            Spacer(modifier = Modifier.height(300.dp))
        }
    }
}

@Composable
private fun ReorderButton(reorder: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable { reorder() }
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.arrow_up_down),
            contentDescription = stringResource(R.string.reorder_habits),
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.reorder_habits),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
@Preview
private fun BottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        ViewBottomSheet(state = rememberStandardBottomSheetState())
    }
}