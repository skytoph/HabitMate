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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.habit.create.IconsColors
import com.github.skytoph.taski.presentation.habit.create.IconsGroup
import com.github.skytoph.taski.presentation.habit.create.SelectIconViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun SelectIconScreen(viewModel: SelectIconViewModel = hiltViewModel(), navigateUp: () -> Unit) {
    val state = viewModel.state()
    Scaffold(topBar = {
        HabitAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
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
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { viewModel.selectIcon(color = color) }
                            .size(32.dp)
                            .background(color = color, shape = CircleShape)
                            .border(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                CircleShape
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
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = icon.name,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .clickable {
                                    viewModel.selectIcon(icon = icon)
                                }
                                .size(48.dp)
                                .background(
                                    if (isSelected) state.value.color else MaterialTheme.colorScheme.secondary,
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
        SelectIconScreen(viewModel = hiltViewModel(), navigateUp = {})
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkSelectIconScreenPreview() {
    TaskiTheme(darkTheme = true) {
        SelectIconScreen(viewModel = hiltViewModel(), navigateUp = {})
    }
}