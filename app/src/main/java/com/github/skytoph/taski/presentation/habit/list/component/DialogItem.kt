package com.github.skytoph.taski.presentation.habit.list.component

import androidx.annotation.StringRes
import com.github.skytoph.taski.R

data class DialogItem(
    @StringRes val dismissLabel: Int,
    @StringRes val confirmLabel: Int,
    @StringRes val text: Int,
    @StringRes val title: Int,
    val onConfirm: () -> Unit = {},
    val onDismiss: () -> Unit = {},
) {
    companion object {
        val notification = DialogItem(
            dismissLabel = R.string.action_cancel,
            confirmLabel = R.string.ok,
            text = R.string.allow_notifications_description,
            title = R.string.allow_notifications_title,
        )
        val alarm = DialogItem(
            dismissLabel = R.string.action_cancel,
            confirmLabel = R.string.ok,
            text = R.string.allow_alarm_description,
            title = R.string.allow_alarms_title,
        )
    }
}