package com.github.skytoph.taski.core.reminder

import androidx.annotation.Keep
import com.github.skytoph.taski.presentation.habit.edit.mapper.FrequencyInterval
import java.io.Serializable

@Keep
data class ReminderItem(
    val id: Long,
    val messageIdentifier: String,
    val uri: String,
    val day: Int,
    val interval: FrequencyInterval,
    val timeMillis: Long,
) : Serializable {

    companion object {
        const val KEY_ITEM = "key_habit_alarm_item"
    }
}