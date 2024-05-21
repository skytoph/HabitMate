package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.presentation.habit.list.component.DeleteDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun BaseAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dismissLabel: String,
    confirmLabel: String,
    text: String,
    title: String,
) {
    AlertDialog(
        text = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmLabel,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = dismissLabel,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    )
}

@Composable
@Preview
fun DeleteDialogPreview() {
    HabitMateTheme {
        DeleteDialog(onDismissRequest = {}, onConfirm = {})
    }
}

@Composable
@Preview
fun DarkDeleteDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        DeleteDialog(onDismissRequest = {}, onConfirm = {})
    }
}