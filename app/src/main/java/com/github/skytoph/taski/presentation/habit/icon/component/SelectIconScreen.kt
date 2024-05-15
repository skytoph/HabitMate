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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.icon.IconState
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup
import com.github.skytoph.taski.presentation.habit.icon.SelectIconEvent
import com.github.skytoph.taski.presentation.habit.icon.SelectIconViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SelectIconScreen(viewModel: SelectIconViewModel = hiltViewModel(), navigateUp: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.color_and_icon_label)
    }

    SelectIcon(
        state = viewModel.state(),
        onSelectColor = { viewModel.onEvent(SelectIconEvent.Update(color = it)) },
        onSelectIcon = { viewModel.onEvent(SelectIconEvent.Update(icon = it)) })
}

@Composable
private fun SelectIcon(
    state: State<IconState>,
    onSelectColor: (Color) -> Unit = {},
    onSelectIcon: (IconResource) -> Unit = {},
) {
    val iconSize = 32.dp
    val iconPadding = 4.dp
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 16.dp),
        columns = GridCells.Adaptive(iconSize + iconPadding),
        contentPadding = PaddingValues(iconPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) },
            contentType = { "title" }) {
            IconGroupLabel(stringResource(R.string.color), iconPadding)
        }
        items(IconsColors.allColors, contentType = { "color" }) { color ->
            val isSelected = color == state.value.color
            ColorItem(onSelectColor, color, iconSize, isSelected)
        }
        IconsGroup.allGroups.forEach { iconGroup ->
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { "title" }) {
                IconGroupLabel(stringResource(iconGroup.title), iconPadding)
            }
            items(iconGroup.icons, contentType = { "icon" }) { iconId ->
                val icon = IconResource.Id(iconId)
                val isSelected = state.value.icon.matches(icon, LocalContext.current)
                IconItem(icon, onSelectIcon, iconSize, isSelected, state.value.color)
            }
        }
    }
}

@Composable
fun IconItem(
    icon: IconResource,
    onSelectIcon: (IconResource) -> Unit = { _ -> },
    iconSize: Dp = 32.dp,
    isSelected: Boolean = false,
    color: Color = MaterialTheme.colorScheme.secondary,
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            icon.vector(context),
            contentDescription = icon.name(context.resources),
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { onSelectIcon(icon) }
                .size(iconSize)
                .background(if (isSelected) color else MaterialTheme.colorScheme.secondary)
                .padding(4.dp),
            tint = Color.White
        )
    }
}

@Composable
fun IconGroupLabel(
    title: String,
    iconPadding: Dp = 8.dp
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = iconPadding),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun ColorItem(
    onSelectColor: (Color) -> Unit,
    color: Color,
    iconSize: Dp,
    isSelected: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onSelectColor(color) }
                .size(iconSize)
                .background(color = color, shape = CircleShape)
                .border(
                    2.dp,
                    if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.Transparent,
                    CircleShape
                )
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SelectIconScreenPreview() {
    HabitMateTheme {
        SelectIcon(remember { mutableStateOf(IconState()) })
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkSelectIconScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        SelectIcon(remember { mutableStateOf(IconState()) })
    }
}