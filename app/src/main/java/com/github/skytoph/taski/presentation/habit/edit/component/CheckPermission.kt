package com.github.skytoph.taski.presentation.habit.edit.component

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.github.skytoph.taski.core.alarm.HabitAlarmChannel
import kotlinx.coroutines.launch

fun checkPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    notificationPermission: String,
    alarmPermission: String,
    showDialog: () -> Unit,
    onGranted: () -> Unit,
) {
    if (ContextCompat.checkSelfPermission(context, notificationPermission) ==
        PackageManager.PERMISSION_GRANTED
    )
        onGranted()
    else
        askForPermission(launcher, showDialog, notificationPermission)
}

fun askForPermission(
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    showDialog: () -> Unit,
    permission: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        launcher.launch(permission)
    else
        showDialog()
}

fun startNotificationSettingsActivity(
    lifecycleScope: LifecycleCoroutineScope,
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    lifecycleScope.launch { launcher.launch(notificationSettingsIntent(context)) }
}

fun notificationSettingsIntent(context: Context): Intent =
    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .addFlags(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT)
        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        .putExtra(Settings.EXTRA_CHANNEL_ID, HabitAlarmChannel.Base.id)

@Composable
fun LaunchNotificationSettingsScreen(
    handleResult: (Boolean) -> Unit,
    context: Context
): ManagedActivityResultLauncher<Intent, ActivityResult> =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val notificationManager = NotificationManagerCompat.from(context)
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
        handleResult(areNotificationsEnabled)
    }
