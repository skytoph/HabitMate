package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.details.components.BaseAlertDialog

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_delete),
        text = stringResource(R.string.delete_habit_confirmation_dialog_title),
        title = stringResource(R.string.delete_habit_confirmation_dialog_title), // todo replace
    )
}

@Composable
fun ArchiveDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_archive),
        text = stringResource(R.string.archive_habit_confirmation_dialog_title),
        title = stringResource(R.string.archive_habit_confirmation_dialog_title), // todo replace
    )
}