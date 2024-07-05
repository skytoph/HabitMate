package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.details.components.BaseAlertDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_delete),
        text = stringResource(R.string.delete_habit_confirmation_dialog_description),
        title = stringResource(R.string.delete_habit_confirmation_dialog_title),
    )
}

@Composable
fun ArchiveDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_archive),
        text = stringResource(R.string.archive_habit_confirmation_dialog_description),
        title = stringResource(R.string.archive_habit_confirmation_dialog_title),
    )
}

@Composable
fun NotificationPermissionDialog(
    dialog: DialogItem
) {
    BaseAlertDialog(
        onDismissRequest = dialog.onDismiss,
        onConfirm = dialog.onConfirm,
        dismissLabel = stringResource(dialog.dismissLabel),
        confirmLabel = stringResource(dialog.confirmLabel),
        text = stringResource(dialog.text),
        title = stringResource(dialog.title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = Color.Transparent
    )
}

@Composable
fun RestoreDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_restore),
        text = stringResource(R.string.restore_habit_confirmation_dialog_description),
        title = stringResource(R.string.restore_habit_confirmation_dialog_title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
    )
}

@Composable
@Preview
private fun DeleteDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        DeleteDialog()
    }
}

@Composable
@Preview
private fun ArchiveDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        ArchiveDialog()
    }
}

@Composable
@Preview
private fun RestoreDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        RestoreDialog()
    }
}
