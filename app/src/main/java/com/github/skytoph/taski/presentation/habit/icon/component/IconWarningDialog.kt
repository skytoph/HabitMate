@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.icon.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.details.components.BaseAlertDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun IconWarningDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: (Boolean) -> Unit = {},
) {
    val checked = remember { mutableStateOf(false) }
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = { onConfirm(checked.value) },
        dismissLabel = stringResource(R.string.action_dismiss),
        confirmLabel = stringResource(R.string.action_confirm),
        title = stringResource(R.string.hide_reminder),
        text = stringResource(R.string.hide_reminder_confirmation_description),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        content = {
            Row(
                modifier = Modifier.clickable { checked.value = !checked.value },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked.value,
                    onCheckedChange = { checked.value = !checked.value })
                Column(modifier = Modifier.animateContentSize()) {
                    Text(
                        text = stringResource(R.string.do_not_show_again),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (checked.value)
                        Text(
                            text = stringResource(R.string.hide_reminder_confirmation_description_permanently),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                }
            }
        }
    )
}

@Composable
@Preview
private fun UnlockIconDialogPreview() {
    HabitMateTheme {
        IconWarningDialog()
    }
}