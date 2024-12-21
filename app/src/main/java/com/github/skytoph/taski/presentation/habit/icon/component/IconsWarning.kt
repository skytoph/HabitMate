package com.github.skytoph.taski.presentation.habit.icon.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.ReminderItem

@Composable
fun IconsWarning(
    modifier: Modifier,
    logIn: () -> Unit,
    dismiss: () -> Unit,
    doNotShowAgain: () -> Unit,
    isLoading: Boolean,
) {
    ReminderItem(
        modifier = modifier,
        logIn = logIn,
        isLoading = isLoading,
        dismiss = dismiss,
        doNotShowAgain = doNotShowAgain,
        icon = ImageVector.vectorResource(R.drawable.sparkles_medium),
        title = stringResource(R.string.icons_warning),
        buttonConfirm = stringResource(R.string.log_in),
        showDoNotShowAgain = true
    )
}