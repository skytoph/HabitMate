package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.github.skytoph.taski.BuildConfig
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsEvent
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsState
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsViewModel
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.component.DeleteDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HabitDetailsScreen(
    viewModel: HabitDetailsViewModel = hiltViewModel(),
    onEditHabit: () -> Unit,
    onDeleteHabit: () -> Unit,
) {
    val colorEdit = MaterialTheme.colorScheme.onSurface
    val colorDelete = MaterialTheme.colorScheme.error
    LaunchedEffect(Unit) {
        val edit = AppBarAction.edit.copy(color = colorEdit, onClick = onEditHabit)
        val delete = AppBarAction.delete.copy(
            color = colorDelete,
            onClick = { viewModel.onEvent(HabitDetailsEvent.ShowDialog(true)) })
        viewModel.initAppBar(
            title = R.string.habit_details_label,
            dropDownItems = listOf(edit, delete)
        )
    }

    val onHideDialog = { viewModel.onEvent(HabitDetailsEvent.ShowDialog(false)) }
    HabitDetails(
        state = viewModel.state(),
        entries = viewModel.entries,
        onHideDialog = onHideDialog,
        onDeleteHabit = { onHideDialog(); onDeleteHabit() },
        onDayClick = { viewModel.habitDone(it) },
        onEditHistory = { viewModel.onEvent(HabitDetailsEvent.EditHistory) },
        isFirstDaySunday = viewModel.settings().value.weekStartsOnSunday.value,
        expandSummary = { viewModel.onEvent(HabitDetailsEvent.ExpandSummary) },
        expandDescription = { viewModel.onEvent(HabitDetailsEvent.ExpandDescription) }
    )
}

@Composable
fun HabitDetails(
    state: State<HabitDetailsState>,
    entries: Flow<PagingData<EditableHistoryUi>>,
    onHideDialog: () -> Unit = {},
    onDeleteHabit: () -> Unit = {},
    onDayClick: (Int) -> Unit = {},
    onEditHistory: () -> Unit = {},
    isFirstDaySunday: Boolean = false,
    expandSummary: () -> Unit = {},
    expandDescription: () -> Unit = {},
) {
    val context = LocalContext.current
    val habit = state.value.habit ?: return

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(size = 44.dp)
                    .background(color = habit.color, shape = RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = habit.icon.vector(context),
                    contentDescription = habit.icon.name(context.resources),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(6.dp),
                    tint = Color.White
                )
            }
            Text(
                text = habit.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (state.value.habit?.description?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .clickable { expandDescription() }
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(8.dp)
                    .animateContentSize(),
            ) {
                Text(
                    text = habit.description,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = if (state.value.isDescriptionExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.frequency_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(8.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LabelWithIcon(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { expandSummary() },
                annotatedText = habit.frequency.summarize(context.resources, isFirstDaySunday, getLocale()),
                icon = ImageVector.vectorResource(R.drawable.calendar),
                maxLines = if (state.value.isSummaryExpanded) Int.MAX_VALUE else 2
            )
            LabelWithIcon(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.goal_value, habit.goal),
                icon = ImageVector.vectorResource(R.drawable.flame),
            )
            LabelWithIcon(
                modifier = Modifier.weight(1f),
                text = habit.reminder.formatted(getLocale(), stringResource(R.string.reminder_turned_off)),
                icon = ImageVector.vectorResource(R.drawable.bell),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.overview_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(8.dp)
        ) {
            LabelWithValue(
                modifier = Modifier.weight(1f),
                label = "total",
                value = state.value.statistics.total.toString(),
                color = state.value.habit?.color
            )
            LabelWithValue(
                modifier = Modifier.weight(1f),
                label = "best streak",
                value = state.value.statistics.bestStreak.toString(),
                color = state.value.habit?.color
            )
            LabelWithValue(
                modifier = Modifier.weight(1f),
                label = "streak",
                value = state.value.statistics.currentStreak.toString(),
                color = state.value.habit?.color
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.history_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        HabitHistory(
            entries = entries,
            goal = habit.goal,
            habitColor = habit.color,
            onEdit = onEditHistory,
            isFirstDaySunday = isFirstDaySunday
        )
        if (BuildConfig.DEBUG)
            Text(text = "streaks: " + state.value.statistics.streaksLength.joinToString(", "))
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (state.value.isDeleteDialogShown)
        DeleteDialog(onDismissRequest = onHideDialog, onConfirm = onDeleteHabit)

    if (state.value.isHistoryEditable)
        EditHistoryDialog(
            entries = entries,
            onDayClick = onDayClick,
            onEdit = onEditHistory,
            habitColor = habit.color,
            goal = habit.goal,
            isFirstDaySunday = isFirstDaySunday
        )
}

@Composable
fun LabelWithIcon(modifier: Modifier = Modifier, text: String, icon: ImageVector, maxLines: Int = 2) =
    LabelWithIcon(modifier = modifier, annotatedText = AnnotatedString(text), icon = icon, maxLines = maxLines)

@Composable
fun LabelWithIcon(
    modifier: Modifier = Modifier,
    annotatedText: AnnotatedString,
    icon: ImageVector,
    maxLines: Int = 2
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = annotatedText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.animateContentSize()
        )
    }
}

@Composable
fun LabelWithValue(
    modifier: Modifier = Modifier,
    color: Color? = null,
    label: String,
    value: String
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = value,
            color = color ?: MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun LabelWithIconAndValue(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    value: String
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = value,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkHabitDetailsScreenPreview(
    @PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>,
    habit: HabitUi = HabitUi(title = "dev", description = "description")
) {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            HabitDetails(
                state = remember { mutableStateOf(HabitDetailsState(habit)) },
                entries = flowOf(PagingData.from(entries)),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HabitDetailsScreenPreview(
    @PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>,
    habit: HabitUi = HabitUi(title = "dev", description = "description")
) {
    HabitMateTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            HabitDetails(
                state = remember { mutableStateOf(HabitDetailsState(habit)) },
                entries = flowOf(PagingData.from(entries)),
            )
        }
    }
}