@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.settings.restore.component

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skytoph.taski.R
import com.skytoph.taski.core.reminder.RefreshRemindersReceiver
import com.skytoph.taski.presentation.core.component.AppBarAction
import com.skytoph.taski.presentation.core.component.DeleteAllBackupsDialog
import com.skytoph.taski.presentation.core.component.DeleteBackupDialog
import com.skytoph.taski.presentation.core.component.EmptyScreen
import com.skytoph.taski.presentation.core.component.LoadingFullscreen
import com.skytoph.taski.presentation.core.component.NotificationPermissionDialog
import com.skytoph.taski.presentation.core.component.RequestPermissionsBackupDialog
import com.skytoph.taski.presentation.core.component.RestoreBackupDialog
import com.skytoph.taski.presentation.core.component.getLocale
import com.skytoph.taski.presentation.core.preview.BackupItemsProvider
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.edit.component.RequestNotificationPermission
import com.skytoph.taski.presentation.habit.edit.component.isPermissionNeeded
import com.skytoph.taski.presentation.settings.backup.BackupMessages
import com.skytoph.taski.presentation.settings.restore.BackupItemUi
import com.skytoph.taski.presentation.settings.restore.RestoreDialogUi
import com.skytoph.taski.presentation.settings.restore.RestoreEvent
import com.skytoph.taski.presentation.settings.restore.RestoreViewModel
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun RestoreScreen(viewModel: RestoreViewModel = hiltViewModel()) {
    val state = viewModel.state()
    val settings = viewModel.settings().collectAsState()
    val locale = getLocale()
    val context = LocalContext.current
    val error = MaterialTheme.colorScheme.error

    val actionDeleteData = remember {
        AppBarAction(
            title = StringResource.ResId(R.string.action_delete_data),
            icon = IconResource.Id(R.drawable.trash),
            color = error,
            onClick = { viewModel.onEvent(RestoreEvent.UpdateDialog(RestoreDialogUi.DeleteAllData)) })
    }

    LaunchedEffect(state.value.items) {
        viewModel.initAppBar(
            title = R.string.restore_title,
            dropDownItems = if (state.value.errorStateMessage == null && state.value.items?.isNotEmpty() == true)
                listOf(actionDeleteData)
            else emptyList()
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.loadItems(locale, settings.value.time24hoursFormat.value, context)
    }

    LaunchedEffect(state.value.items) {
        if (state.value.items?.isEmpty() == true)
            viewModel.onEvent(RestoreEvent.ErrorFullscreen(StringResource.ResId(R.string.no_backup)))
    }

    LaunchedEffect(state.value.refreshingReminders) {
        if (state.value.refreshingReminders) {
            viewModel.onEvent(RestoreEvent.RefreshingReminders(false))
            context.applicationContext.sendBroadcast(Intent(context, RefreshRemindersReceiver::class.java))
            viewModel.showMessage(BackupMessages.importSucceededMessage)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackupItemsList(
            items = state.value.items ?: emptyList(),
            isLoading = state.value.isLoading,
            error = state.value.errorStateMessage?.getString(context),
            restore = { viewModel.onEvent(RestoreEvent.UpdateDialog(RestoreDialogUi.Restore(it))) },
            showContextMenu = { viewModel.onEvent(RestoreEvent.ShowContextMenu(it)) })
        AnimatedVisibility(visible = state.value.isLoading, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingFullscreen()
            }
        }
    }
    state.value.contextMenuItem?.let { item ->
        BackupItemBottomSheet(
            title = item.date,
            hideBottomSheet = { viewModel.onEvent(RestoreEvent.ShowContextMenu()) },
            restore = { viewModel.onEvent(RestoreEvent.UpdateDialog(RestoreDialogUi.Restore(item))) },
            delete = { viewModel.onEvent(RestoreEvent.UpdateDialog(RestoreDialogUi.Delete(item))) })
    }

    state.value.dialog?.let { dialog ->
        DialogItem(
            dialog = dialog,
            restoreSettings = state.value.restoreSettings,
            restore = {
                viewModel.onEvent(RestoreEvent.ShowContextMenu())
                viewModel.downloadBackup(it.id, settings.value.time24hoursFormat.value, context)
            },
            delete = {
                viewModel.onEvent(RestoreEvent.ShowContextMenu())
                viewModel.delete(it.id, locale, settings.value.time24hoursFormat.value, context)
            },
            deleteAllData = { viewModel.deleteAllData() },
            requestPermissions = { viewModel.onEvent(RestoreEvent.RequestPermissions(true)) },
            dismiss = { viewModel.onEvent(RestoreEvent.UpdateDialog()) },
            restoreSettingsClick = { viewModel.onEvent(RestoreEvent.UpdateRestoreSettings) })
    }

    state.value.permissionDialog?.let { dialog ->
        NotificationPermissionDialog(dialog)
    }

    RequestNotificationPermission(
        requestPermissionDialog = { viewModel.onEvent(RestoreEvent.UpdatePermissionDialog(it)) },
        permissionGranted = { isGranted ->
            if (isGranted) viewModel.onEvent(RestoreEvent.RefreshingReminders(true))
            else viewModel
        },
        initialize = false,
        content = { requestPermission ->
            if (state.value.requestingPermission) {
                viewModel.onEvent(RestoreEvent.RequestPermissions(false))
                if (isPermissionNeeded(context))
                    requestPermission()
                else viewModel.showMessage(BackupMessages.importSucceededMessage)
            }
        }
    )
}

@Composable
fun BackupItemsList(
    items: List<BackupItemUi>,
    isLoading: Boolean = false,
    error: String? = null,
    restore: (BackupItemUi) -> Unit = {},
    showContextMenu: (BackupItemUi) -> Unit = {}
) {
    Crossfade(targetState = error != null, label = "crossfade_backup_items") {
        if (it && error != null)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                EmptyScreen(
                    title = error,
                    icon = ImageVector.vectorResource(id = R.drawable.folder_large)
                )
            }
        else LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items) { index, item ->
                BackupItem(
                    title = item.date,
                    description = item.size,
                    isLoading = isLoading,
                    onClick = { restore(item) },
                    onLongClick = { showContextMenu(item) })
                if (index != items.lastIndex)
                    HorizontalDivider(
                        modifier = Modifier
                            .widthIn(max = 520.dp)
                            .padding(horizontal = 16.dp)
                    )
            }
        }
    }
}

@Composable
fun BackupItem(title: String, description: String, onClick: () -> Unit, onLongClick: () -> Unit, isLoading: Boolean) {
    Column(
        modifier = Modifier
            .widthIn(max = 520.dp)
            .fillMaxWidth()
            .combinedClickable(enabled = !isLoading, onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 32.dp, vertical = 12.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.backup_item_size, description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun DialogItem(
    dialog: RestoreDialogUi,
    restoreSettings: Boolean,
    restore: (BackupItemUi) -> Unit,
    delete: (BackupItemUi) -> Unit,
    restoreSettingsClick: () -> Unit,
    deleteAllData: () -> Unit,
    requestPermissions: () -> Unit,
    dismiss: () -> Unit
) = when (dialog) {
    is RestoreDialogUi.Restore -> RestoreBackupDialog(
        date = dialog.item.date,
        checked = restoreSettings,
        checkboxClick = restoreSettingsClick,
        onConfirm = { restore(dialog.item) },
        onDismissRequest = dismiss
    )

    is RestoreDialogUi.Delete -> DeleteBackupDialog(
        date = dialog.item.date,
        onConfirm = { delete(dialog.item) },
        onDismissRequest = dismiss
    )

    is RestoreDialogUi.DeleteAllData -> DeleteAllBackupsDialog(
        onConfirm = deleteAllData,
        onDismissRequest = dismiss
    )

    is RestoreDialogUi.RequestPermissions -> RequestPermissionsBackupDialog(
        onConfirm = requestPermissions,
        onDismissRequest = dismiss
    )
}

@Composable
@Preview
fun BackupPreview(@PreviewParameter(BackupItemsProvider::class) items: List<BackupItemUi>) {
    HabitMateTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            BackupItemsList(items = items)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingFullscreen()
            }
        }
    }
}