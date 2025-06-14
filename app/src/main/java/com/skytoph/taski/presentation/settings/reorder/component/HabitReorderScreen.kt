@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.skytoph.taski.presentation.settings.reorder.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.component.EmptyScreen
import com.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.skytoph.taski.presentation.core.preview.HabitsProvider
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.presentation.settings.reorder.ReorderHabitsEvent
import com.skytoph.taski.presentation.settings.reorder.ReorderHabitsViewModel
import com.skytoph.taski.ui.theme.HabitMateTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

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

    val settings = viewModel.settings().collectAsState()
    val state = viewModel.state()

    HabitsReorder(
        habits = viewModel.habits().value,
        currentSort = settings.value.view.sortBy.optionUi().title.getString(context),
        showApplyManualOrder = state.value.isReminderShown,
        onSwap = { from, to -> viewModel.swap(from, to) },
        applyManualOrder = { viewModel.applyManualOrder() },
        dismissReminder = { viewModel.onEvent(ReorderHabitsEvent.UpdateReminder(false)) }
    )
}

@Composable
private fun HabitsReorder(
    habits: List<HabitUi>,
    currentSort: String = "color",
    showApplyManualOrder: Boolean = true,
    onSwap: (Int, Int) -> Unit = { _, _ -> },
    applyManualOrder: () -> Unit = {},
    dismissReminder: () -> Unit = {}
) {
    val lazyListState = rememberLazyListState()
    val state = rememberReorderableLazyListState(lazyListState) { from, to ->
        onSwap(from.index - 2, to.index - 2)
    }

    if (habits.isEmpty()) EmptyScreen(
        title = stringResource(R.string.list_of_habits_is_empty_label),
        icon = ImageVector.vectorResource(R.drawable.sparkles_large)
    )
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AnimatedVisibility(
                visible = showApplyManualOrder,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ApplyManualOrder(
                    modifier = Modifier
                        .widthIn(max = 520.dp)
                        .fillMaxWidth(),
                    currentSort = currentSort,
                    apply = applyManualOrder,
                    dismiss = dismissReminder
                )
            }
        }
        item {
            ReorderableItem(state = state, key = 0, enabled = false) {}
        }
        items(habits, key = { it.id }) { habit ->
            ReorderableItem(state = state, key = habit.id) { isDragging ->
                HabitReorderingItem(
                    modifier = Modifier
                        .longPressDraggableHandle()
                        .widthIn(max = 520.dp),
                    habit = habit,
                    borderColor = if (isDragging) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ApplyManualOrder(
    modifier: Modifier = Modifier,
    currentSort: String,
    apply: () -> Unit,
    dismiss: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_up_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.apply_manual_sorting_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = stringResource(R.string.apply_manual_sorting_description, currentSort),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClick = dismiss)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.action_dismiss),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClick = apply)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.action_apply),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Composable
fun HabitReorderingItem(
    modifier: Modifier,
    habit: HabitUi,
    borderColor: Color,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(2.dp, borderColor, CardDefaults.shape),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            HabitTitleWithIcon(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp),
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
@Preview(showBackground = true)
private fun DarkHabitReorderingPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        val state = remember { mutableStateOf(false) }
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { state.value = !state.value }) {
            HabitsReorder(habits = habits.map { it.habit })
        }
    }
}