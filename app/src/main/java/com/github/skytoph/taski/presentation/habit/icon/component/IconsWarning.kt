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
    modifier: Modifier = Modifier,
    logIn: () -> Unit = {},
    dismiss: () -> Unit = {},
    isLoading: Boolean = false,
) {
    ReminderItem(
        modifier = modifier,
        confirm = logIn,
        isLoading = isLoading,
        dismiss = dismiss,
        icon = ImageVector.vectorResource(R.drawable.sparkles_medium),
        text = stringResource(R.string.icons_warning),
        buttonConfirm = stringResource(R.string.log_in),
    )
}