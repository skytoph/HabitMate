package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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
@OptIn(ExperimentalMaterial3Api::class)
fun ViewBottomSheet(
    view: HabitsView = HabitsView(),
    hideBottomSheet: () -> Unit = {},
    selectViewType: (ViewOption) -> Unit = {},
    selectSorting: (SortOption) -> Unit = {},
    selectFilter: (FilterOption) -> Unit = {},
    reorder: () -> Unit = {}
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state
    ) {
        Column {
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
private fun ReorderButton(reorder: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(CircleShape)
            .clickable { reorder() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Text(
            text = "reorder habits",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun BottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        ViewBottomSheet()
    }
}