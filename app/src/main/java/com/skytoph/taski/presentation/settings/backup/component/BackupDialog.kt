package com.skytoph.taski.presentation.settings.backup.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.skytoph.taski.presentation.core.component.ClearDialog
import com.skytoph.taski.presentation.core.component.DeleteAccountDialog
import com.skytoph.taski.presentation.core.component.ExportDialog
import com.skytoph.taski.presentation.core.component.ImportDialog
import com.skytoph.taski.presentation.core.component.RequestPermissionsBackupDialog
import com.skytoph.taski.presentation.core.component.SignOutDialog
import com.skytoph.taski.presentation.settings.backup.BackupDialogUi
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun BackupDialog(
    dialog: BackupDialogUi,
    export: () -> Unit,
    import: () -> Unit,
    clear: () -> Unit,
    signOut: () -> Unit,
    deleteAccount: () -> Unit,
    requestPermissions: () -> Unit,
    dismiss: () -> Unit,
    dismissPermissions: () -> Unit,
) = when (dialog) {
    BackupDialogUi.Export -> ExportDialog(onConfirm = export, onDismissRequest = dismiss)
    BackupDialogUi.Import -> ImportDialog(onConfirm = import, onDismissRequest = dismiss)
    BackupDialogUi.SignOut -> SignOutDialog(onConfirm = signOut, onDismissRequest = dismiss)
    BackupDialogUi.DeleteAccount -> DeleteAccountDialog(onConfirm = deleteAccount, onDismissRequest = dismiss)
    BackupDialogUi.Clear -> ClearDialog(onConfirm = clear, onDismissRequest = dismiss)
    BackupDialogUi.RequestPermissions ->
        RequestPermissionsBackupDialog(onConfirm = requestPermissions, onDismissRequest = dismissPermissions)
}

@Composable
@Preview
private fun ExportDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        ExportDialog()
    }
}

@Composable
@Preview
private fun ImportDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        ImportDialog()
    }
}

@Composable
@Preview
private fun DeleteAccountDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        DeleteAccountDialog()
    }
}

@Composable
@Preview
private fun RequestPermissionsBackupDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        RequestPermissionsBackupDialog()
    }
}

@Composable
@Preview
private fun SignOutDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        SignOutDialog()
    }
}