package com.github.skytoph.taski.presentation.habit.edit.frequency

import android.content.res.Resources
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.create.GoalState
import java.util.Calendar

sealed interface FrequencyState {
    fun summarize(resources: Resources): String

    data class Custom(
        val timesCount: GoalState = GoalState(3),
        val typeCount: GoalState = GoalState(1),
        val frequencyType: FrequencyCustomType = FrequencyCustomType.Week,
    ) : FrequencyState {
        override fun summarize(resources: Resources): String {
            val type = resources.getString(frequencyType.title)
            val resId = R.string.frequency_summary_custom
            return resources.getString(resId, timesCount.value, typeCount.value, type)
        }
    }

    data class Daily(
        val days: List<Int> = (1..7).toList()
    ) : FrequencyState {
        override fun summarize(resources: Resources): String {
            val arg = days.joinToString(separator = " ,")
            return resources.getString(R.string.frequency_summary_daily, arg)
        }
    }

    data class Monthly(
        val days: List<Int> = listOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    ) : FrequencyState {
        override fun summarize(resources: Resources): String {
            val arg = days.joinToString(separator = " ,")
            return resources.getString(R.string.frequency_summary_monthly, arg)
        }
    }
}