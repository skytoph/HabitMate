package com.github.skytoph.taski.core.alarm

import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import java.io.Serializable

data class AlarmItem(
    val id: Long,
    val title: String,
    @StringRes val message: Int,
    val icon: String,
    @ColorInt val color: Int,
    val uri: String,
    val day: Int,
    val interval: Int = 1,
    val timeMillis: Long,
) : Serializable {

    companion object {
        const val KEY_ITEM = "key_habit_alarm_item"
    }
}