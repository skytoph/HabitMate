package com.github.skytoph.taski.presentation.settings.reorder.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.EmptyScreen
import com.github.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.settings.reorder.ReorderHabitsViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun HabitReorderScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: ReorderHabitsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(
            title = R.string.reorder_habits, canNavigateUp = true, menuItems = emptyList()
        )
    }
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) viewModel.saveOrder(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    HabitsReorder(
        habits = viewModel.habits().value,
        onSwap = { from, to -> viewModel.swap(from, to) },
    )
}

@Composable
private fun HabitsReorder(
    habits: List<HabitUi>,
    onSwap: (Int, Int) -> Unit = { _, _ -> }
) {
    val state =
        rememberReorderableLazyListState(onMove = { from, to -> onSwap(from.index, to.index) })

    if (habits.isEmpty()) EmptyScreen(
        title = stringResource(R.string.list_of_habits_is_empty_label),
        icon = ImageVector.vectorResource(R.drawable.sparkles)
    )

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .padding(16.dp)
            .reorderable(state)
            .detectReorderAfterLongPress(state),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(habits, key = { it.id }) { habit ->
            ReorderableItem(state, key = habit.id) { isDragging ->
                val elevation = animateDpAsState(
                    targetValue = if (isDragging) 16.dp else 0.dp,
                    label = "anim_reorder_elevation"
                )
                val borderColor =
                    if (isDragging) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                HabitReorderingItem(habit = habit, elevation.value, borderColor)
            }
        }
    }
}

@Composable
fun HabitReorderingItem(
    habit: HabitUi,
    elevation: Dp,
    borderColor: Color,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(2.dp, borderColor, CardDefaults.shape)
            .shadow(elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            HabitTitleWithIcon(
                modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp, top = 8.dp),
                icon = habit.icon.vector(LocalContext.current),
                color = habit.color,
                title = habit.title,
            )
            Box(Modifier.padding(dimensionResource(id = R.dimen.habit_icon_padding))) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.align_justify),
                    contentDescription = stringResource(id = R.string.reorder_habits),
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.habit_icon_size))
                        .padding(4.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun DarkHabitReorderingPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        HabitsReorder(habits = habits.map { it.habit })
    }
}