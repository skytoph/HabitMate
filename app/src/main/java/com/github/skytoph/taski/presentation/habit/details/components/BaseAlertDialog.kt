@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.component.ExportDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun BaseAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dismissLabel: String,
    confirmLabel: String,
    text: AnnotatedString,
    title: String,
    confirmColor: Color = MaterialTheme.colorScheme.error,
    confirmContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
    icon: ImageVector? = null
) {
    AlertDialog(
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
                        color = confirmContainerColor, shape = MaterialTheme.shapes.extraLarge
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
fun BaseAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dismissLabel: String,
    confirmLabel: String,
    text: String,
    title: String,
    confirmColor: Color = MaterialTheme.colorScheme.error,
    confirmContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
    icon: ImageVector? = null
) = BaseAlertDialog(
    onDismissRequest = onDismissRequest,
    onConfirm = onConfirm,
    dismissLabel = dismissLabel,
    confirmLabel = confirmLabel,
    text = AnnotatedString(text),
    title = title,
    confirmColor = confirmColor,
    confirmContainerColor = confirmContainerColor,
    icon = icon
)

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
        ExportDialog()
    }
}