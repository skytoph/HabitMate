package com.skytoph.taski.presentation.habit.details

import android.content.Context
import com.skytoph.taski.presentation.core.state.StringResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

data class BarItemUi(
    val timestamp: Long = 0L,
    val timesDone: Int = 0,
    val dateLabel: StringResource = StringResource.Value(""),
    val isEmpty: Boolean = false
) {
    fun getDisplayDate(locale: Locale, context: Context): String =
        if (isEmpty) ""
        else SimpleDateFormat(dateLabel.getString(context), locale).format(Date(timestamp))

    fun percentDone(maxValue: Int) = min(timesDone.toFloat().div(maxValue), 1f)
}