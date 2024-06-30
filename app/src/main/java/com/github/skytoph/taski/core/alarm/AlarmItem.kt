package com.github.skytoph.taski.core.alarm

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.github.skytoph.taski.presentation.core.state.IconResource
import java.util.Calendar

data class AlarmItem(
    val habitId: Long,
    val title: String,
    @StringRes val message: Int,
    val icon: IconResource,
    val calendar: Calendar,
    val interval: Long,
    val type: Int
) {
    fun putToIntent(context: Context, intent: Intent) = intent.apply {
        putExtra(KEY_ID, habitId)
        putExtra(KEY_MESSAGE, message)
        putExtra(KEY_TITLE, title)
        putExtra(KEY_ICON, icon.name(context.resources))
    }

    companion object {
        const val KEY_ID = "key_habit_id"
        const val KEY_TITLE = "key_title"
        const val KEY_MESSAGE = "key_message"
        const val KEY_ICON = "key_icon"
    }
}