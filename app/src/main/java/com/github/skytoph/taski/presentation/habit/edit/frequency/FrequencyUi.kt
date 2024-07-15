package com.github.skytoph.taski.presentation.habit.edit.frequency

import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.text.AnnotatedString
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.alarm.AlarmItem
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.core.alarm.ScheduleAlarm
import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.presentation.core.format.annotate
import com.github.skytoph.taski.presentation.core.format.getWeekDisplayName
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.mapper.FrequencyInterval
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitDateMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.MapToDates
import java.util.Calendar
import java.util.Locale

sealed interface FrequencyUi : MapToDates, FrequencyInterval, ScheduleAlarm {
    fun summarize(resources: Resources, isFirstDaySunday: Boolean, locale: Locale): AnnotatedString
    fun updateType(add: Int): FrequencyUi = this
    fun update(day: Int): FrequencyUi
    fun map(): Frequency
    fun isEveryday(): Boolean
    val name: String
    val times: Int

    companion object {
        const val NOW_DEFAULT = true
    }

    data class Custom(
        val timesCount: GoalState = GoalState(3),
        val typeCount: GoalState = GoalState(1),
        val frequencyType: FrequencyCustomType = FrequencyCustomType.Week,
    ) : FrequencyUi {
        override val name: String = "custom"
        override val times: Int = timesCount.value
        override val interval: Int = typeCount.value * frequencyType.interval

        override fun summarize(resources: Resources, isFirstDaySunday: Boolean, locale: Locale): AnnotatedString {
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

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean): Map<Int, Calendar> =
            frequencyType.dates(mapper, isFirstDaySunday, timesCount.value, typeCount.value)

        override fun schedule(scheduler: AlarmScheduler, context: Context, items: List<AlarmItem>) =
            frequencyType.schedule(scheduler, context, items)

        override fun map(): Frequency =
            Frequency.Custom(timesCount.value, typeCount.value, frequencyType.map())

        override fun isEveryday(): Boolean = !timesCount.canBeIncreased
    }

    data class Daily(
        val days: Set<Int> = (1..7).toSet()
    ) : FrequencyUi {
        override val name: String = "daily"
        override val times: Int = days.size
        override val interval: Int = 1

        override fun summarize(resources: Resources, isFirstDaySunday: Boolean, locale: Locale): AnnotatedString =
            annotate(
                initialString = resources.getString(R.string.frequency_summary_daily),
                arguments = days,
                transform = { getWeekDisplayName(locale, it) })

        override fun update(day: Int) = copy(days = days.toMutableSet().apply {
            if (days.contains(day) && days.size > 1) remove(day) else add(day)
        }).let { if (it.isEveryday()) Everyday(it) else it }

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean): Map<Int, Calendar> =
            mapper.mapDaily(isFirstDaySunday, days)

        override fun schedule(scheduler: AlarmScheduler, context: Context, items: List<AlarmItem>) =
            scheduler.scheduleRepeating(context, items)

        override fun map(): Frequency = Frequency.Daily(days)

        override fun isEveryday(): Boolean = days.size == 7
    }

    data class Monthly(
        val days: Set<Int> = setOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    ) : FrequencyUi {
        override val name: String = "monthly"
        override val times: Int = days.size
        override val interval: Int = FrequencyInterval.INTERVAL_MONTH

        override fun summarize(resources: Resources, isFirstDaySunday: Boolean, locale: Locale): AnnotatedString =
            annotate(
                initialString = resources.getString(R.string.frequency_summary_monthly),
                arguments = days,
                transform = { it.toString() })

        override fun update(day: Int) = copy(days = days.toSortedSet().apply {
            if (days.contains(day) && days.size > 1) remove(day) else add(day)
        }).let { if (it.isEveryday()) Everyday(it) else it }

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean): Map<Int, Calendar> =
            mapper.mapMonthly(days)

        override fun schedule(scheduler: AlarmScheduler, context: Context, items: List<AlarmItem>) =
            scheduler.schedule(context, items)

        override fun map(): Frequency = Frequency.Monthly(days)

        override fun isEveryday(): Boolean = days.size == 31
    }

    data class Everyday(val frequency: FrequencyUi) : FrequencyUi {
        override val name: String = frequency.name
        override val times: Int = 1
        override val interval: Int = 1

        override fun summarize(resources: Resources, isFirstDaySunday: Boolean, locale: Locale): AnnotatedString =
            AnnotatedString(resources.getString(R.string.everyday))

        override fun update(day: Int): FrequencyUi = frequency.update(day)

        override fun updateType(add: Int): FrequencyUi = frequency.updateType(add)

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean): Map<Int, Calendar> =
            mapper.mapEveryday()

        override fun schedule(scheduler: AlarmScheduler, context: Context, items: List<AlarmItem>) =
            scheduler.scheduleRepeating(context, items)

        override fun map(): Frequency = frequency.map()

        override fun isEveryday(): Boolean = frequency.isEveryday()
    }
}