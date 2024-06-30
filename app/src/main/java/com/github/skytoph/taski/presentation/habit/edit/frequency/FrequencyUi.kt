package com.github.skytoph.taski.presentation.habit.edit.frequency

import android.content.res.Resources
import androidx.compose.ui.text.AnnotatedString
import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.presentation.core.format.annotate
import com.github.skytoph.taski.presentation.core.format.getWeekDisplayName
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.mapper.FrequencyInterval
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitDateMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.MapToDates
import java.util.Calendar
import java.util.Locale

sealed interface FrequencyUi : MapToDates, FrequencyInterval {
    fun summarize(resources: Resources, locale: Locale): AnnotatedString
    fun updateType(add: Int): FrequencyUi = this
    fun update(day: Int): FrequencyUi
    fun map(): Frequency
    fun isEveryday(): Boolean
    val name: String

    data class Custom(
        val timesCount: GoalState = GoalState(3),
        val typeCount: GoalState = GoalState(1),
        val frequencyType: FrequencyCustomType = FrequencyCustomType.Week,
    ) : FrequencyUi {
        override val name: String = "custom"
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString {
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
            return copy(timesCount = times).let { if (it.isEveryday()) Everyday(it) else it }
        }

        override fun updateType(add: Int): FrequencyUi {
            val value = typeCount.value + add
            val type = frequencyType.type(value)
            val times = frequencyType.times(timesCount.value, value)
            return copy(typeCount = type, timesCount = times)
        }

        override fun dates(mapper: HabitDateMapper): List<Calendar> =
            frequencyType.dates(mapper, timesCount.value, typeCount.value)

        override fun interval(): Int = typeCount.value * frequencyType.interval

        override fun map(): Frequency =
            Frequency.Custom(timesCount.value, typeCount.value, frequencyType.map())

        override fun isEveryday(): Boolean = !timesCount.canBeIncreased
    }

    data class Daily(
        val days: Set<Int> = (1..7).toSet()
    ) : FrequencyUi {
        override val name: String = "daily"
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString =
            annotate(
                initialString = resources.getString(R.string.frequency_summary_daily),
                arguments = days,
                transform = { getWeekDisplayName(locale, it) })

        override fun update(day: Int) = copy(days = days.toSortedSet().apply {
            if (days.contains(day) && days.size > 1) remove(day) else add(day)
        }).let { if (it.isEveryday()) Everyday(it) else it }

        override fun dates(mapper: HabitDateMapper): List<Calendar> = mapper.mapDaily(days)

        override fun interval(): Int = 1

        override fun map(): Frequency = Frequency.Daily(days)

        override fun isEveryday(): Boolean = days.size == 7
    }

    data class Monthly(
        val days: Set<Int> = setOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    ) : FrequencyUi {
        override val name: String = "monthly"
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString =
            annotate(
                initialString = resources.getString(R.string.frequency_summary_monthly),
                arguments = days,
                transform = { it.toString() })

        override fun update(day: Int) = copy(days = days.toSortedSet().apply {
            if (days.contains(day) && days.size > 1) remove(day) else add(day)
        }).let { if (it.isEveryday()) Everyday(it) else it }

        override fun dates(mapper: HabitDateMapper): List<Calendar> = mapper.mapMonthly(days)

        override fun interval(): Int = 1

        override fun map(): Frequency = Frequency.Monthly(days)

        override fun isEveryday(): Boolean = days.size == 31
    }

    data class Everyday(val frequency: FrequencyUi) : FrequencyUi {
        override val name: String = frequency.name
        override fun summarize(resources: Resources, locale: Locale): AnnotatedString =
            AnnotatedString(resources.getString(R.string.everyday))

        override fun update(day: Int): FrequencyUi = frequency.update(day)

        override fun updateType(add: Int): FrequencyUi = frequency.updateType(add)

        override fun dates(mapper: HabitDateMapper): List<Calendar> = mapper.mapEveryday()

        override fun interval(): Int = 1

        override fun map(): Frequency = frequency.map()

        override fun isEveryday(): Boolean = frequency.isEveryday()
    }
}