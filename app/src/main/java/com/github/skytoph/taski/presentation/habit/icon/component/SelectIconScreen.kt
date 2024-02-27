package com.github.skytoph.taski.presentation.habit.icon.component

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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.habit.icon.IconState
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup
import com.github.skytoph.taski.presentation.habit.icon.SelectIconEvent
import com.github.skytoph.taski.presentation.habit.icon.SelectIconViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun SelectIconScreen(viewModel: SelectIconViewModel = hiltViewModel(), navigateUp: () -> Unit) {
    SelectIcon(
        state = viewModel.state(),
        navigateUp = navigateUp,
        onSelectColor = { viewModel.onEvent(SelectIconEvent.Update(color = it)) },
        onSelectIcon = { viewModel.onEvent(SelectIconEvent.Update(icon = it)) })
}

@Composable
private fun SelectIcon(
    state: State<IconState>,
    navigateUp: () -> Unit = {},
    onSelectColor: (Color) -> Unit = {},
    onSelectIcon: (ImageVector) -> Unit = {},
) {
    Scaffold(topBar = {
        HabitAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = stringResource(R.string.color_and_icon_label),
            navigateUp = navigateUp,
        )
    }) { paddingValue ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValue),
            columns = GridCells.Adaptive(56.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(IconsColors.allColors, contentType = { "color" }) { color ->
                val isSelected = color == state.value.color
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onSelectColor(color) }
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
                item(
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = { "title" }) {
                    Text(
                        text = stringResource(iconGroup.title),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                items(iconGroup.icons, key = { it.name }, contentType = { "icon" }) { icon ->
                    val isSelected = icon.name == state.value.icon.name
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = icon.name,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { onSelectIcon(icon) }
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
        SelectIcon(remember { mutableStateOf(IconState()) })
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkSelectIconScreenPreview() {
    TaskiTheme(darkTheme = true) {
        SelectIcon(remember { mutableStateOf(IconState()) })
    }
}