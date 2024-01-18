package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun DeleteAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        text = {
            Text(
                text = "Do you want to delete the habit?",
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
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}

@Composable
@Preview
fun DeleteDialogPreview() {
    TaskiTheme {
        DeleteAlertDialog(onDismissRequest = {}, onConfirm = {})
    }
}

@Composable
@Preview
fun DarkDeleteDialogPreview() {
    TaskiTheme(darkTheme = true) {
        DeleteAlertDialog(onDismissRequest = {}, onConfirm = {})
    }
}