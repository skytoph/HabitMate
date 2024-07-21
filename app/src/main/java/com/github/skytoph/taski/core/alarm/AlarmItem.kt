package com.github.skytoph.taski.core.alarm

import com.github.skytoph.taski.presentation.habit.edit.mapper.FrequencyInterval
import java.io.Serializable

data class AlarmItem(
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