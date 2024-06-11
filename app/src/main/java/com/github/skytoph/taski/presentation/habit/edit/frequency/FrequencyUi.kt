package com.github.skytoph.taski.presentation.habit.edit.frequency

import android.content.res.Resources
import androidx.compose.ui.text.AnnotatedString
import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.presentation.core.format.annotate
import com.github.skytoph.taski.presentation.core.format.getWeekDisplayName
import com.github.skytoph.taski.presentation.habit.create.GoalState
import java.util.Calendar
import java.util.Locale

sealed interface FrequencyUi {
    fun summarize(resources: Resources, locale: Locale): AnnotatedString
    fun updateType(add: Int): FrequencyUi = this
    fun update(day: Int): FrequencyUi
    fun map(): Frequency
    val name: String

    data class Custom(
        val timesCount: GoalState = GoalState(3),
        val typeCount: GoalState = GoalState(1),
        val frequencyType: FrequencyCustomType = FrequencyCustomType.Week,
    ) : FrequencyUi {
        override val name: String = "custom"
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString {
            if (!timesCount.canBeIncreased) return AnnotatedString(resources.getString(R.string.everyday))
            val type = resources.getQuantityString(frequencyType.title, typeCount.value)
            val resId = R.string.frequency_summary_custom
            val string = resources.getString(resId, timesCount.value, typeCount.value, type)
            return annotate(
                string = string,
                arguments = listOf(timesCount.value.toString(), typeCount.value.toString())
            )
        }

        override fun update(add: Int): FrequencyUi {
            val value = timesCount.value + add
            val times = frequencyType.times(value, typeCount.value)
            return copy(timesCount = times)
        }

        override fun updateType(add: Int): FrequencyUi {
            val value = typeCount.value + add
            val type = frequencyType.type(value)
            val times = frequencyType.times(timesCount.value, value)
            return copy(typeCount = type, timesCount = times)
        }

        override fun map(): Frequency =
            Frequency.Custom(timesCount.value, typeCount.value, frequencyType.map())
    }

    data class Daily(
        val days: Set<Int> = (1..7).toSet()
    ) : FrequencyUi {
        override val name: String = "daily"
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString =
            if (days.size == 7) AnnotatedString(resources.getString(R.string.everyday))
            else annotate(
                initialString = resources.getString(R.string.frequency_summary_daily),
                arguments = days,
                transform = { getWeekDisplayName(locale, it) })

        override fun update(day: Int) = copy(days = days.toSortedSet().apply {
            if (days.contains(day) && days.size > 1) remove(day) else add(day)
        })

        override fun map(): Frequency = Frequency.Daily(days)
    }

    data class Monthly(
        val days: Set<Int> = setOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    ) : FrequencyUi {
        override val name: String = "monthly"
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString =
            if (days.size == 31) AnnotatedString(resources.getString(R.string.everyday))
            else annotate(
                initialString = resources.getString(R.string.frequency_summary_monthly),
                arguments = days,
                transform = { it.toString() })

        override fun update(day: Int) = copy(days = days.toSortedSet().apply {
            if (days.contains(day) && days.size > 1) remove(day) else add(day)
        })

        override fun map(): Frequency = Frequency.Monthly(days)
    }
}