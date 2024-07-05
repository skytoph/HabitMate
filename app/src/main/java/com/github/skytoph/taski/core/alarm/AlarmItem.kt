package com.github.skytoph.taski.core.alarm

import android.app.AlarmManager
import android.net.Uri
import androidx.annotation.StringRes
import java.io.Serializable
import java.util.Calendar

data class AlarmItem(
    val id: Long,
    val title: String,
    @StringRes val message: Int,
    val icon: String,
    val calendar: Calendar,
    val day: Int,
    @Transient val uri: Uri,
    @Transient val interval: Long = AlarmManager.INTERVAL_DAY,
    @Transient val type: Int = AlarmManager.RTC_WAKEUP
) : Serializable {

    fun intervalEquals(interval: Int): Boolean =
        interval * AlarmManager.INTERVAL_DAY == this.interval

    companion object {
        const val KEY_ITEM = "key_habit_alarm_item"
    }
}