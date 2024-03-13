package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.DeleteIconButton
import com.github.skytoph.taski.presentation.core.component.EditIconButton
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsEvent
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsState
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsViewModel
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HabitDetailsScreen(
    viewModel: HabitDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onEditHabit: () -> Unit,
) {
    HabitDetails(
        state = viewModel.state(),
        entries = viewModel.entries,
        navigateUp = navigateUp,
        onEdit = onEditHabit,
        onDeleteClick = { viewModel.onEvent(HabitDetailsEvent.ShowDialog(true)) },
        onDeleteHabit = { viewModel.deleteHabit() },
        onHideDialog = { viewModel.onEvent(HabitDetailsEvent.ShowDialog(false)) },
        onDayClick = { viewModel.habitDone(it) }
    ) { viewModel.onEvent(HabitDetailsEvent.EditHistory) }
}

@Composable
fun HabitDetails(
    state: State<HabitDetailsState>,
    entries: Flow<PagingData<EditableHistoryUi>>,
    navigateUp: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDeleteHabit: () -> Unit = {},
    onHideDialog: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDayClick: (Int) -> Unit = {},
    onEditHistory: () -> Unit = {},
) {
    val context = LocalContext.current
    val habit = state.value.habit ?: return
    val actionButtons = listOf(
        EditIconButton(MaterialTheme.colorScheme.onSurface, action = onEdit),
        DeleteIconButton(MaterialTheme.colorScheme.error, onDeleteClick),
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        HabitAppBar(
            label = stringResource(R.string.habit_details),
            navigateUp = navigateUp,
            menuItems = actionButtons
        )


        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(size = 40.dp)
                    .background(color = habit.color, shape = RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(habit.icon.id(context)),
                    contentDescription = habit.icon.name(context.resources),
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
            Text(text = habit.title, style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabelWithIcon(
                text = stringResource(R.string.goal_value, habit.goal),
                icon = Icons.Default.CalendarMonth
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.history_label),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        HabitHistory(
            entries = entries,
            goal = habit.goal,
            habitColor = habit.color,
            onEdit = onEditHistory,
            isEditButtonVisible = true,
            isEditable = state.value.isHistoryEditable,
            onDayClick = onDayClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (state.value.isDeleteDialogShown)
        DeleteAlertDialog(
            onDismissRequest = onHideDialog,
            onConfirm = {
                onDeleteHabit()
                onHideDialog()
                navigateUp()
            })
}

@Composable
fun LabelWithIcon(text: String, icon: ImageVector) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitDetailsScreenPreview(
    @PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>,
    habit: HabitUi = HabitUi(title = "Dev")
) {
    HabitMateTheme(darkTheme = false) {
        HabitDetails(
            state = remember { mutableStateOf(HabitDetailsState(habit)) },
            entries = flowOf(PagingData.from(entries))
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitDetailsScreenPreview(
    @PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>,
    habit: HabitUi = HabitUi(title = "dev")
) {
    HabitMateTheme(darkTheme = true) {
        HabitDetails(
            state = remember { mutableStateOf(HabitDetailsState(habit)) },
            entries = flowOf(PagingData.from(entries))
        )
    }
}