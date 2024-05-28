package com.github.skytoph.taski.presentation.habit.edit.frequency

import android.content.res.Resources
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.create.GoalState
import java.util.Calendar

sealed interface FrequencyState {
    fun summarize(resources: Resources): String
    fun updateType(add: Int): FrequencyState = this
    fun update(day: Int): FrequencyState

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

        override fun update(add: Int): FrequencyState {
            val value = timesCount.value + add
            val times = frequencyType.times(value, typeCount.value)
            return copy(timesCount = times)
        }

        override fun updateType(add: Int): FrequencyState {
            val value = typeCount.value + add
            val type = frequencyType.type(timesCount.value, value)
            val times = frequencyType.times(timesCount.value, value)
            return copy(typeCount = type, timesCount = times)
        }
    }

    data class Daily(
        val days: Set<Int> = (1..7).toSet()
    ) : FrequencyState {
        override fun summarize(resources: Resources): String {
            val arg = days.joinToString(separator = " ,")
            return resources.getString(R.string.frequency_summary_daily, arg)
        }

        override fun update(day: Int) = copy(
            days = days.toMutableSet().apply { if (days.contains(day)) remove(day) else add(day) })
    }

    data class Monthly(
        val days: Set<Int> = setOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    ) : FrequencyState {
        override fun summarize(resources: Resources): String {
            val arg = days.joinToString(separator = " ,")
            return resources.getString(R.string.frequency_summary_monthly, arg)
        }

        override fun update(day: Int) = copy(
            days = days.toMutableSet().apply { if (days.contains(day)) remove(day) else add(day) })
    }
}