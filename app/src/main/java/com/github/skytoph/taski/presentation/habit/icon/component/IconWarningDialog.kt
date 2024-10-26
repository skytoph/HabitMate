@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.icon.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.details.components.BaseAlertDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun IconWarningDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_dismiss),
        confirmLabel = stringResource(R.string.action_confirm),
        title = stringResource(R.string.hide_reminder),
        text = stringResource(R.string.hide_reminder_confirmation_description),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    )
}

@Composable
@Preview
private fun UnlockIconDialogPreview() {
    HabitMateTheme {
        IconWarningDialog()
    }
}