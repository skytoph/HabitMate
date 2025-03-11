package com.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.skytoph.taski.R
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun CheckboxDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    checkboxClick: () -> Unit = {},
    isChecked: Boolean,
    text: AnnotatedString,
    title: String,
    icon: ImageVector? = null,
    checkboxText: String = stringResource(R.string.do_not_show_again),
    confirmLabel: String = stringResource(R.string.action_confirm),
    dismissLabel: String = stringResource(R.string.action_dismiss),
    confirmColor: Color = MaterialTheme.colorScheme.error,
    confirmContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
    checkboxSupportingText: String? = null
) = BaseAlertDialog(
    onDismissRequest = onDismissRequest,
    onConfirm = onConfirm,
    dismissLabel = dismissLabel,
    confirmLabel = confirmLabel,
    text = text,
    title = title,
    confirmColor = confirmColor,
    confirmContainerColor = confirmContainerColor,
    icon = icon,
    content = {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = checkboxClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { checkboxClick() })
            Column(modifier = Modifier.animateContentSize()) {
                Text(
                    text = checkboxText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (checkboxSupportingText != null && isChecked)
                    Text(
                        text = checkboxSupportingText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
            }
        }
    }
)

@Composable
@Preview
private fun DarkDeleteDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        CheckboxDialog(
            isChecked = true,
            text = AnnotatedString("Confirm action"),
            title = "Confirm",
        )
    }
}