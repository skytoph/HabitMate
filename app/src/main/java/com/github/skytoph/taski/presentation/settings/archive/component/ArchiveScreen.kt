package com.github.skytoph.taski.presentation.settings.archive.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.component.DeleteDialog
import com.github.skytoph.taski.presentation.settings.archive.HabitArchiveEvent
import com.github.skytoph.taski.presentation.settings.archive.HabitsArchiveViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ArchiveScreen(
    viewModel: HabitsArchiveViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.title_archive)
    }

    val state = viewModel.state()
    val habits = state.value.habits
    val messageArchive = stringResource(R.string.message_habit_unarchived)
    val messageDelete = stringResource(R.string.message_habit_deleted)
    if (habits.isEmpty())
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "archive is empty")
        }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(habits) { habit ->
            ArchivedHabitItem(
                habit = habit,
                unarchive = { viewModel.unarchive(it, messageArchive) },
                delete = { viewModel.onEvent(HabitArchiveEvent.UpdateDeleteDialog(it)) })
        }
    }
    state.value.deleteHabitById?.let { id ->
        DeleteDialog(
            onConfirm = { viewModel.delete(id, messageDelete) },
            onDismissRequest = { viewModel.onEvent(HabitArchiveEvent.UpdateDeleteDialog(null)) })
    }
}

@Composable
fun ArchivedHabitItem(
    habit: HabitUi,
    unarchive: (Long) -> Unit = {},
    delete: (Long) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column {
            HabitTitleWithIcon(
                icon = habit.icon.vector(LocalContext.current),
                color = habit.color,
                title = habit.title,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ArchiveActionButton(
                    title = "delete",
                    icon = ImageVector.vectorResource(R.drawable.trash),
                    id = habit.id,
                    onClick = delete,
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                ArchiveActionButton(
                    title = "restore",
                    icon = ImageVector.vectorResource(R.drawable.archive_up),
                    id = habit.id,
                    onClick = unarchive,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun ArchiveActionButton(
    title: String,
    icon: ImageVector,
    id: Long,
    onClick: (Long) -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(color = containerColor)
            .clickable { onClick(id) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(18.dp),
            tint = contentColor
        )
        Text(
            text = title,
            modifier = Modifier.padding(start = 4.dp),
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
private fun ArchivedHabitItemPreview() {
    HabitMateTheme {
        ArchivedHabitItem(HabitUi(title = "habit"))
    }
}

@Composable
@Preview
private fun DarkArchivedHabitItemPreview() {
    HabitMateTheme(darkTheme = true) {
        ArchivedHabitItem(HabitUi(title = "habit"))
    }
}