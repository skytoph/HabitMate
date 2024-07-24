@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    confirmColor: Color = MaterialTheme.colorScheme.error,
    confirmContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
) {
    AlertDialog(
        text = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false,
            ) {
                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier.background(
                        color = confirmContainerColor, shape = MaterialTheme.shapes.medium
                    )
                ) {
                    Text(
                        text = confirmLabel,
                        color = confirmColor,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        dismissButton = {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false,
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = dismissLabel,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
    )
}

@Composable
@Preview
private fun DeleteDialogPreview() {
    HabitMateTheme {
        DeleteDialog()
    }
}

@Composable
@Preview
private fun DarkDeleteDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        DeleteDialog()
    }
}