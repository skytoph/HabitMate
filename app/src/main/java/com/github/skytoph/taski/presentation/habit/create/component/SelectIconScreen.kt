package com.github.skytoph.taski.presentation.habit.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.habit.create.EditHabitViewModel
import com.github.skytoph.taski.presentation.habit.create.IconsColors
import com.github.skytoph.taski.presentation.habit.create.IconsGroup
import com.github.skytoph.taski.presentation.habit.create.SelectIcon
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun SelectIconScreen(viewModel: SelectIcon, navigateUp: () -> Unit) {
    val state = viewModel.state()
    Scaffold(topBar = {
        HabitAppBar(
            label = "color and icon",
            navigateUp = navigateUp,
            isSaveButtonVisible = false
        )
    }) { paddingValue ->

        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValue),
            columns = GridCells.Adaptive(60.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(IconsColors.allColors) { color ->
                val isSelected = color == state.value.color
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.selectIcon(color = color) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(color = color, shape = CircleShape)
                            .border(
                                1.dp, if (isSelected) Color.Cyan else Color.Transparent, CircleShape
                            )
                    )
                }
            }
            IconsGroup.allGroups.forEach { iconGroup ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(iconGroup.title),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                items(iconGroup.icons) { icon ->
                    val isSelected = icon.name == state.value.icon.name
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            viewModel.selectIcon(icon = icon)
                        }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = icon.name,
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    if (isSelected) state.value.color else MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(8.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SelectIconScreenPreview() {
    TaskiTheme {
        SelectIconScreen(
            viewModel = hiltViewModel<EditHabitViewModel>(),
            navigateUp = {}
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkSelectIconScreenPreview() {
    TaskiTheme(darkTheme = true) {
        SelectIconScreen(
            viewModel = hiltViewModel<EditHabitViewModel>(),
            navigateUp = {}
        )
    }
}