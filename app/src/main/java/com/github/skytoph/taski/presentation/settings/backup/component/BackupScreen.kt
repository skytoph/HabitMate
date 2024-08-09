package com.github.skytoph.taski.presentation.settings.backup.component

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.reminder.RefreshRemindersReceiver
import com.github.skytoph.taski.presentation.core.component.DeleteAccountDialog
import com.github.skytoph.taski.presentation.core.component.ExportDialog
import com.github.skytoph.taski.presentation.core.component.ImportDialog
import com.github.skytoph.taski.presentation.core.component.LoadingFullscreen
import com.github.skytoph.taski.presentation.core.component.LoadingItems
import com.github.skytoph.taski.presentation.core.component.NotificationPermissionDialog
import com.github.skytoph.taski.presentation.core.component.RequestPermissionsBackupDialog
import com.github.skytoph.taski.presentation.core.component.SignOutDialog
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.habit.edit.component.RequestNotificationPermission
import com.github.skytoph.taski.presentation.habit.edit.component.isPermissionNeeded
import com.github.skytoph.taski.presentation.settings.backup.BackupDialogUi
import com.github.skytoph.taski.presentation.settings.backup.BackupEvent
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages
import com.github.skytoph.taski.presentation.settings.backup.BackupViewModel
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.launch

@Composable
fun BackupScreen(viewModel: BackupViewModel = hiltViewModel(), restoreBackup: () -> Unit) {
    val context = LocalContext.current
    val locale = getLocale()
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state()
    val contract = ActivityResultContracts.StartActivityForResult()

    LaunchedEffect(Unit) {
        viewModel.checkConnection(context)
        viewModel.loadProfile(context)
    }

    val startForResult = rememberLauncherForActivityResult(contract) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent -> viewModel.signInWithFirebase(intent, context) }
        }
    }

    val launcherImport = rememberLauncherForActivityResult(contract = contract) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            result.data?.data?.let { uri ->
                viewModel.import(context.contentResolver, uri)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_backup_title)
    }

    LaunchedEffect(state.value.uriShareFile) {
        if (state.value.uriShareFile != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = context.getString(R.string.backup_mimetype)
            intent.putExtra(Intent.EXTRA_STREAM, state.value.uriShareFile)
            viewModel.onEvent(BackupEvent.ShareFile())
            context.startActivity(intent)
        }
    }

    Backup(
        isImportLoading = state.value.isImportLoading,
        isExportLoading = state.value.isExportLoading,
        isDriveBackupLoading = state.value.isDriveBackupLoading,
        isProfileLoading = state.value.isProfileLoading,
        isSigningIn = state.value.isSigningInLoading,
        isInternetConnected = state.value.isInternetConnected,
        restoreBackup = restoreBackup,
        profile = state.value.profile,
        lastTimeBackupSaved = viewModel.mapBackupTime(state.value.lastBackupSavedTime, context, locale),
        logIn = {
            viewModel.onEvent(BackupEvent.IsSigningIn(true))
            coroutineScope.launch {
                startForResult.launch(SignInWithGoogle.DriveScope.getClient(context).signInIntent)
            }
        },
        saveBackup = { viewModel.saveBackupOnDrive(context) },
        export = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.Export)) },
        import = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.Import)) },
        signOut = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.SignOut)) },
        deleteAccount = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.DeleteAccount)) }
    )

    state.value.dialog?.let { dialog ->
        BackupDialog(
            dialog = dialog,
            export = { viewModel.export(context) },
            import = { launcherImport.launch(Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" }) },
            signOut = { viewModel.signOut(context) },
            deleteAccount = { viewModel.deleteAccount(context) },
            requestPermissions = { viewModel.onEvent(BackupEvent.RequestPermissions(true)) },
            dismiss = { viewModel.onEvent(BackupEvent.UpdateDialog()) })
    }

    state.value.permissionDialog?.let { dialog ->
        NotificationPermissionDialog(dialog)
    }

    RequestNotificationPermission(
        requestPermissionDialog = { viewModel.onEvent(BackupEvent.UpdatePermissionDialog(it)) },
        permissionGranted = { isGranted ->
            if (isGranted) {
                context.sendBroadcast(Intent(RefreshRemindersReceiver.ACTION))
                viewModel.showMessage(BackupMessages.importSucceededMessage)
            }
        },
        content = { requestPermission ->
            if (state.value.requestingPermission) {
                viewModel.onEvent(BackupEvent.RequestPermissions(false))
                if (isPermissionNeeded(context))
                    requestPermission()
                else viewModel.showMessage(BackupMessages.importSucceededMessage)
            }
        }
    )
}

@Composable
private fun Backup(
    export: () -> Unit = {},
    import: () -> Unit = {},
    signOut: () -> Unit = {},
    deleteAccount: () -> Unit = {},
    logIn: () -> Unit = {},
    saveBackup: () -> Unit = {},
    restoreBackup: () -> Unit = {},
    isImportLoading: Boolean = false,
    isExportLoading: Boolean = true,
    isDriveBackupLoading: Boolean = false,
    isProfileLoading: Boolean = false,
    isSigningIn: Boolean = true,
    isLoadingFullscreen: Boolean = false,
    isInternetConnected: Boolean = true,
    profile: ProfileUi? = ProfileUi(email = "email@gmail.com", name = "Name"),
    lastTimeBackupSaved: String? = "28.09.24 12:00",
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.google_drive_backup),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                when {
                    !isInternetConnected ->
                        Text(
                            text = stringResource(R.string.backup_no_connection),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                    isProfileLoading ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 278.dp)
                        ) {
                            LoadingItems()
                        }

                    profile == null ->
                        LogInItem(
                            logIn = logIn,
                            isLoading = isSigningIn
                        )

                    else ->
                        DriveBackup(
                            profile = profile,
                            lastTimeBackupSaved = lastTimeBackupSaved,
                            createBackup = saveBackup,
                            restoreBackup = restoreBackup,
                            signOut = signOut,
                            isBackupLoading = isDriveBackupLoading,
                            deleteAccount = deleteAccount
                        )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.local_backup),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LocalBackup(export, isExportLoading, import, isImportLoading)
        }
        AnimatedVisibility(visible = isLoadingFullscreen) {
            if (isLoadingFullscreen)
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

@Composable
private fun BackupDialog(
    dialog: BackupDialogUi,
    export: () -> Unit,
    import: () -> Unit,
    signOut: () -> Unit,
    deleteAccount: () -> Unit,
    requestPermissions: () -> Unit,
    dismiss: () -> Unit
) = when (dialog) {
    BackupDialogUi.Export -> ExportDialog(onConfirm = export, onDismissRequest = dismiss)
    BackupDialogUi.Import -> ImportDialog(onConfirm = import, onDismissRequest = dismiss)
    BackupDialogUi.SignOut -> SignOutDialog(onConfirm = signOut, onDismissRequest = dismiss)
    BackupDialogUi.DeleteAccount -> DeleteAccountDialog(onConfirm = deleteAccount, onDismissRequest = dismiss)
    BackupDialogUi.RequestPermissions ->
        RequestPermissionsBackupDialog(onConfirm = requestPermissions, onDismissRequest = dismiss)
}

@Composable
private fun LocalBackup(
    export: () -> Unit,
    isExportLoading: Boolean,
    import: () -> Unit,
    isImportLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        ButtonWithLoadingFull(
            title = stringResource(R.string.export_title),
            onClick = export,
            isLoading = isExportLoading,
            loadingText = stringResource(R.string.loading_export),
            modifier = Modifier.fillMaxWidth(),
        )
        HorizontalDivider()
        ButtonWithLoadingFull(
            title = stringResource(R.string.import_title),
            onClick = import,
            isLoading = isImportLoading,
            loadingText = stringResource(R.string.loading_import),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun DriveBackup(
    profile: ProfileUi,
    createBackup: () -> Unit,
    restoreBackup: () -> Unit,
    signOut: () -> Unit = {},
    deleteAccount: () -> Unit,
    isBackupLoading: Boolean,
    lastTimeBackupSaved: String?,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileItem(profile = profile, lastTimeBackupSaved = lastTimeBackupSaved)
            Spacer(modifier = Modifier.height(16.dp))
            ButtonWithLoading(
                title = stringResource(R.string.create_backup_on_drive),
                onClick = createBackup,
                isLoading = isBackupLoading,
                loadingText = stringResource(R.string.loading_backup),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
        ) {
            ButtonWithLoadingFull(
                title = stringResource(R.string.restore_item),
                onClick = restoreBackup,
                isLoading = false,
                modifier = Modifier.fillMaxWidth(),
            )
            HorizontalDivider()
            ButtonWithLoadingFull(
                title = stringResource(R.string.delete_account_item),
                onClick = deleteAccount,
                textColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
            ButtonWithLoadingFull(
                title = stringResource(R.string.sign_out_item),
                onClick = signOut,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ProfileItem(profile: ProfileUi, lastTimeBackupSaved: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profile.imageUri)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.circle_user_round)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.width(IntrinsicSize.Max)) {
            Text(
                text = profile.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = profile.email,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Crossfade(
                targetState = lastTimeBackupSaved == null,
                label = "loading_backup_time_crossfade",
                animationSpec = tween(durationMillis = 150),
                modifier = Modifier
                    .heightIn(min = 16.dp)
                    .fillMaxWidth(),
            ) {
                if (it) Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    LoadingItems(
                        itemColor = MaterialTheme.colorScheme.primary,
                        item = ImageVector.vectorResource(id = R.drawable.sparkle_filled),
                    )
                } else Text(
                    text = lastTimeBackupSaved!!,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                )
            }
        }
    }
}

@Composable
private fun LogInItem(
    logIn: () -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.backup_with_google_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithLoading(
            title = stringResource(R.string.log_in),
            onClick = logIn,
            isLoading = isLoading,
            loadingText = stringResource(R.string.signing_in),
            enabledColor = MaterialTheme.colorScheme.primary,
            disabledColor = MaterialTheme.colorScheme.secondaryContainer,
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
    isLoading: Boolean,
    tint: Color? = null
) {
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
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint ?: LocalContentColor.current,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Crossfade(targetState = isLoading, label = "loading_crossfade", animationSpec = tween(durationMillis = 150)) {
            if (it) LoadingItems(
                itemColor = MaterialTheme.colorScheme.primary,
                item = ImageVector.vectorResource(id = R.drawable.sparkle_filled)
            ) else Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        ButtonWithLoading(
            title = title,
            onClick = onClick,
            isLoading = isLoading,
            loadingText = loadingText,
            enabledColor = MaterialTheme.colorScheme.onTertiaryContainer,
            disabledColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ButtonWithLoading(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    loadingText: String = "",
    enabledColor: Color = MaterialTheme.colorScheme.primary,
    disabledColor: Color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
    textColor: Color = Color.White,
) {
    val color = remember { Animatable(if (isLoading) disabledColor else enabledColor) }
    LaunchedEffect(isLoading) {
        color.animateTo(if (isLoading) disabledColor else enabledColor)
    }
    Box(modifier = modifier
        .background(
            color = color.value,
            shape = MaterialTheme.shapes.large
        )
        .clip(MaterialTheme.shapes.small)
        .clickable(enabled = !isLoading) { onClick() }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .animateContentSize(),
        contentAlignment = Alignment.Center) {
        Crossfade(
            targetState = isLoading,
            label = "backup_button_crossfade",
            animationSpec = tween(durationMillis = 150)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = if (it) loadingText else title,
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                if (it) LoadingItems(spaceSize = 4.dp)
            }
        }
    }
}

@Composable
private fun ButtonWithLoadingFull(
    title: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    loadingText: String = "",
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier,
) {
    val disabled = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val enabled = MaterialTheme.colorScheme.primaryContainer
    val color = remember { Animatable(if (isLoading) disabled else enabled) }
    LaunchedEffect(isLoading) {
        color.animateTo(targetValue = if (isLoading) disabled else enabled, animationSpec = tween(durationMillis = 400))
    }
    Box(modifier = modifier
        .background(color = color.value)
        .clickable(enabled = !isLoading) { onClick() }
        .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center) {
        Crossfade(
            targetState = isLoading,
            label = "backup_button_crossfade",
            animationSpec = tween(durationMillis = 400)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = if (it) loadingText else title,
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                if (it) LoadingItems(spaceSize = 4.dp)
            }
        }
    }
}

@Composable
@Preview
fun BackupPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(Color.Black)) { Backup() }
    }
}