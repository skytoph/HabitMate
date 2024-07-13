@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.OptionsDropdown
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ViewBottomSheet(
    state: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    view: HabitsView = HabitsView(),
    hideBottomSheet: () -> Unit = {},
    selectViewType: (ViewType) -> Unit = {},
    selectSorting: (SortHabits) -> Unit = {},
    selectFilter: (FilterHabits) -> Unit = {},
    reorder: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                title = stringResource(R.string.view_filter),
                options = FilterHabits.options,
                selected = view.filterBy,
                selectOption = selectFilter
            )
            HorizontalDivider()
            OptionsDropdown(
                title = stringResource(R.string.view_sort),
                options = SortHabits.options,
                selected = view.sortBy,
                selectOption = selectSorting
            )
            HorizontalDivider()
            ButtonWithIcon(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                onClick = reorder,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                title = stringResource(R.string.reorder_habits),
                icon = ImageVector.vectorResource(R.drawable.arrow_up_down),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(300.dp))
        }
    }
}

@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    backgroundColor: Color,
    title: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(20.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            fontWeight = FontWeight.Normal
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