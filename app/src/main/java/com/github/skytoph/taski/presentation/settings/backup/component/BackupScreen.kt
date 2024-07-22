package com.github.skytoph.taski.presentation.settings.backup.component

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.component.ButtonWithBackground
import com.github.skytoph.taski.presentation.core.component.LoadingItems
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.settings.backup.BackupEvent
import com.github.skytoph.taski.presentation.settings.backup.BackupViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun BackupScreen(viewModel: BackupViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state = viewModel.state()
    val contract = ActivityResultContracts.StartActivityForResult()
    val successfulImportMessage = stringResource(id = R.string.success_import_message)
    val successfulImportTitle = stringResource(id = R.string.success_import_title)
    val errorImportMessage = stringResource(id = R.string.error_import)
    val errorImportTitle = stringResource(id = R.string.import_error_title)
    val errorExportMessage = stringResource(id = R.string.error_export)
    val errorExportTitle = stringResource(id = R.string.error_export_title)
    val importIcon = IconResource.Id(id = R.drawable.folder_input)
    val exportIcon = IconResource.Id(id = R.drawable.folder_output)
    val launcherImport = rememberLauncherForActivityResult(contract = contract) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            result.data?.data?.let { uri ->
                val success =
                    SnackbarMessage(message = successfulImportMessage, title = successfulImportTitle, icon = importIcon)
                val error =
                    SnackbarMessage(message = errorImportMessage, title = errorImportTitle, icon = importIcon)
                viewModel.import(context.contentResolver, uri, success, error)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_backup_title)
    }

    LaunchedEffect(state.value.uri) {
        if (state.value.uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/octet-stream"
            intent.putExtra(Intent.EXTRA_STREAM, state.value.uri)
            viewModel.onEvent(BackupEvent.ShareFile())
            context.startActivity(intent)
        }
    }

    Backup(
        isImportLoading = state.value.importLoading,
        isExportLoading = state.value.exportLoading,
        export = {
            val error =
                SnackbarMessage(message = errorExportMessage, title = errorExportTitle, icon = exportIcon)
            viewModel.export(context, error)
        },
        import = { launcherImport.launch(Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" }) })
}

@Composable
private fun Backup(
    export: () -> Unit = {},
    import: () -> Unit = {},
    isImportLoading: Boolean = true,
    isExportLoading: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        BackupItem(
            onClick = export,
            title = stringResource(R.string.export_title),
            text = stringResource(R.string.export_description),
            loadingText = "Preparing the data...",
            icon = ImageVector.vectorResource(R.drawable.folder_output),
            isLoading = isExportLoading
        )
        BackupItem(
            onClick = import,
            title = stringResource(R.string.import_title),
            text = stringResource(R.string.import_description),
            loadingText = "Restoring the data...",
            icon = ImageVector.vectorResource(R.drawable.folder_input),
            isLoading = isImportLoading
        )
    }
}

@Composable
private fun BackupItem(
    onClick: () -> Unit,
    title: String,
    text: String,
    loadingText: String,
    icon: ImageVector,
    isLoading: Boolean
) {
    val enabled = MaterialTheme.colorScheme.primary
    val disabled = MaterialTheme.colorScheme.secondaryContainer
    val color = remember { Animatable(enabled) }
    LaunchedEffect(isLoading) {
        color.animateTo(if (isLoading) disabled else enabled)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        ButtonWithBackground(
            onClick = onClick,
            text = if (isLoading) loadingText else title,
            background = color.value,
            enabled = !isLoading
        )
        Crossfade(targetState = isLoading, label = "loading_crossfade") {
            if (it) LoadingItems(
                itemColor = MaterialTheme.colorScheme.primary,
                item = ImageVector.vectorResource(id = R.drawable.sparkle_filled)
            ) else Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BackupLoading() {

}

@Composable
@Preview
fun BackupPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(Color.Black)) { Backup() }
    }
}