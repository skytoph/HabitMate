@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skytoph.taski.R
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun TimePickerDialog(
    onConfirm: (Int, Int) -> Unit = { _, _ -> },
    onDismissRequest: () -> Unit = {},
    initialHour: Int = 12,
    initialMinute: Int = 0,
) {
    val state = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute)
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        TimePickerContent(
            title = stringResource(R.string.select_time),
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(R.string.action_cancel),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        ) {
            TimePicker(state = state)
        }
    }
}

@Composable
private fun TimePickerContent(
    title: String,
    dismissButton: @Composable() (() -> Unit)?,
    confirmButton: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            content()
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                dismissButton?.invoke()
                confirmButton()
            }
        }
    }
}

@Composable
@Preview
fun TimePickerDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        Box { TimePickerDialog() }
    }
}