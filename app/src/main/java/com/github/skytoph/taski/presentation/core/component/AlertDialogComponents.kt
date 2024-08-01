package com.github.skytoph.taski.presentation.core.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.details.components.BaseAlertDialog
import com.github.skytoph.taski.presentation.habit.list.component.DialogItem
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    text: String,
    title: String,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_delete),
        text = text,
        title = title,
    )
}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    text: AnnotatedString,
    title: String,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_delete),
        text = text,
        title = title,
    )
}

@Composable
fun ArchiveDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_archive),
        text = stringResource(R.string.archive_habit_confirmation_dialog_description),
        title = stringResource(R.string.archive_habit_confirmation_dialog_title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = Color.Transparent
    )
}

@Composable
fun NotificationPermissionDialog(
    dialog: DialogItem
) {
    BaseAlertDialog(
        onDismissRequest = dialog.onDismiss,
        onConfirm = dialog.onConfirm,
        dismissLabel = stringResource(dialog.dismissLabel),
        confirmLabel = stringResource(dialog.confirmLabel),
        text = stringResource(dialog.text),
        title = stringResource(dialog.title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
    )
}

@Composable
fun RestoreDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    text: String,
    title: String,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_restore),
        text = text,
        title = title,
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
    )
}

@Composable
fun RestoreDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    text: AnnotatedString,
    title: String,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_restore),
        text = text,
        title = title,
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
    )
}

@Composable
fun ExportDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_export),
        text = stringResource(id = R.string.export_description),
        title = stringResource(id = R.string.export_title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        icon = ImageVector.vectorResource(id = R.drawable.folder_output)
    )
}

@Composable
fun ImportDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_import),
        text = stringResource(id = R.string.import_description),
        title = stringResource(id = R.string.import_title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        icon = ImageVector.vectorResource(id = R.drawable.folder_input)
    )
}

@Composable
fun SignOutDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_sign_out),
        text = stringResource(id = R.string.sign_out_description),
        title = stringResource(id = R.string.sign_out_title),
        confirmColor = MaterialTheme.colorScheme.primary,
        confirmContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
    )
}

@Composable
fun DeleteAccountDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val description = buildAnnotatedBoldString(
        stringResource(id = R.string.delete_account_description_confirmation) to SpanStyle(),
        " " + stringResource(id = R.string.delete_account_description_warning) to SpanStyle(fontWeight = FontWeight.Bold),
        "\n\n" + stringResource(id = R.string.delete_account_description_details) to SpanStyle(fontSize = 10.sp),
        divider = ""
    )
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        dismissLabel = stringResource(R.string.action_cancel),
        confirmLabel = stringResource(R.string.action_delete),
        text = description,
        title = stringResource(id = R.string.delete_account_title),
    )
}

@Composable
fun RestoreBackupDialog(
    date: String,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val description = buildAnnotatedBoldString(
        date to SpanStyle(),
        "\n" + stringResource(id = R.string.restore_dialog_description_confirmation) to SpanStyle(),
        stringResource(id = R.string.restore_dialog_description_warning) to SpanStyle(fontWeight = FontWeight.Bold)
    )
    RestoreDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        text = description,
        title = stringResource(id = R.string.restore_backup_title),
    )
}

@Composable
fun DeleteBackupDialog(
    date: String,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val description = buildAnnotatedBoldString(
        date to SpanStyle(fontWeight = FontWeight.Bold),
        "\n" + stringResource(id = R.string.delete_backup_dialog_description_confirmation) to SpanStyle(),
        stringResource(id = R.string.delete_backup_dialog_description_warning) to SpanStyle(fontWeight = FontWeight.Bold)
    )
    DeleteDialog(
        title = stringResource(R.string.delete_backup_title),
        text = description,
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun DeleteAllBackupsDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val description = buildAnnotatedBoldString(
        stringResource(id = R.string.delete_all_data_description_confirmation) to SpanStyle(),
        " " + stringResource(id = R.string.delete_all_data_description_warning) to SpanStyle(fontWeight = FontWeight.Bold),
        "\n\n" + stringResource(id = R.string.delete_all_data_description_details) to SpanStyle(fontSize = 10.sp),
        divider = ""
    )
    DeleteDialog(
        title = stringResource(R.string.delete_backup_title),
        text = description,
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest
    )
}

fun buildAnnotatedBoldString(
    vararg textToBold: Pair<String, SpanStyle?>,
    divider: String = "\n"
): AnnotatedString = buildAnnotatedString {
    textToBold.forEachIndexed { index, item ->
        item.second?.let { style ->
            withStyle(style = style) { append(item.first) }
        } ?: append(item.first)
        if (index < textToBold.lastIndex)
            append(divider)
    }
}

@Composable
@Preview
private fun DialogPreview() {
    HabitMateTheme(darkTheme = true) {
        DeleteAllBackupsDialog()
    }
}

@Composable
@Preview
private fun DialogPreviewa() {
    HabitMateTheme(darkTheme = true) {
        DeleteAccountDialog()
    }
}