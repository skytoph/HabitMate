package com.skytoph.taski.presentation.habit.edit.frequency

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.skytoph.taski.R
import com.skytoph.taski.core.reminder.ReminderItem
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.core.reminder.ScheduleReminder
import com.skytoph.taski.domain.habit.Frequency
import com.skytoph.taski.presentation.habit.create.GoalState
import com.skytoph.taski.presentation.habit.edit.mapper.FrequencyInterval
import com.skytoph.taski.presentation.habit.edit.mapper.HabitDateMapper
import com.skytoph.taski.presentation.habit.edit.mapper.MapToDatesCustom

sealed class FrequencyCustomType : MapToDatesCustom, ScheduleReminder {
    @get:StringRes
    abstract val title: Int
    @get:PluralsRes
    abstract val titlePlural: Int

    abstract val maxTimes: Int
    abstract val maxType: Int
    abstract val interval: FrequencyInterval
    open val minType: Int = 1

    fun times(times: Int, type: Int): GoalState {
        val maxValue = maxTimes * type
        val value = if (times > maxValue) maxValue else times
        return GoalState(
            value = value, canBeIncreased = times < maxValue, canBeDecreased = times > 1
        )
    }

    fun type(type: Int): GoalState {
        val value = if (type < minType) minType else if (type < maxType) type else maxType
        return GoalState(
            value = value, canBeIncreased = value < maxType, canBeDecreased = value > minType
        )
    }

    override fun schedule(scheduler: ReminderScheduler, items: List<ReminderItem>) =
        scheduler.scheduleRepeating(items)

    abstract fun map(): Frequency.Custom.Type

    data object Day : FrequencyCustomType() {
        override val titlePlural: Int = R.plurals.day_label
        override val title: Int = R.string.day_label_one
        override val maxTimes: Int = 1
        override val maxType: Int = 365
        override val interval: FrequencyInterval = FrequencyInterval.Day(1, false)

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Day

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean, timesCount: Int, typeCount: Int) =
            mapper.mapCustomDay(timesCount, typeCount)
    }

    data object Week : FrequencyCustomType() {
        override val titlePlural: Int = R.plurals.week_label
        override val title: Int = R.string.week_label_one
        override val maxTimes: Int = 7
        override val maxType: Int = 100
        override val interval: FrequencyInterval = FrequencyInterval.Day(7, false)

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Week

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean, timesCount: Int, typeCount: Int) =
            mapper.mapCustomWeek(isFirstDaySunday, timesCount, typeCount)
    }

    data object Month : FrequencyCustomType() {
        override val titlePlural: Int = R.plurals.month_label
        override val title: Int = R.string.month_label_one
        override val maxTimes: Int = 31
        override val maxType: Int = 12
        override val interval: FrequencyInterval = FrequencyInterval.Month(1, true)

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Month

        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean, timesCount: Int, typeCount: Int) =
            mapper.mapCustomMonth(timesCount, typeCount)

        override fun schedule(scheduler: ReminderScheduler, items: List<ReminderItem>) =
            scheduler.schedule(items)
    }
}