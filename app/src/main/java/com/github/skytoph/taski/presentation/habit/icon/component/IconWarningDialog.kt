@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.icon.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.details.components.CheckboxDialog
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun IconWarningDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: (Boolean) -> Unit = {},
) {
    val checked = remember { mutableStateOf(false) }
    CheckboxDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = { onConfirm(checked.value) },
        checkboxClick = { checked.value = !checked.value },
        text = AnnotatedString(stringResource(R.string.hide_reminder_confirmation_description)),
        title = stringResource(R.string.hide_reminder),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        isChecked = checked.value,
        checkboxSupportingText = stringResource(R.string.hide_reminder_confirmation_description_permanently),
    )
}

@Composable
@Preview
private fun UnlockIconDialogPreview() {
    HabitMateTheme {
        IconWarningDialog()
    }
}