package com.github.skytoph.taski.presentation.habit.edit.frequency

import androidx.annotation.PluralsRes
import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitDateMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.MapToDatesCustom

sealed class FrequencyCustomType : MapToDatesCustom {
    @get:PluralsRes
    abstract val title: Int

    abstract val maxTimes: Int
    abstract val maxType: Int
    open val minType: Int = 1

    open val interval: Int = 1

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

    abstract fun map(): Frequency.Custom.Type

    data object Day : FrequencyCustomType() {
        override val title: Int = R.plurals.day_label
        override val maxTimes: Int = 1
        override val maxType: Int = 365

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Day

        override fun dates(mapper: HabitDateMapper, timesCount: Int, typeCount: Int) =
            mapper.mapCustomDay(timesCount, typeCount)
    }

    data object Week : FrequencyCustomType() {
        override val title: Int = R.plurals.week_label
        override val maxTimes: Int = 7
        override val maxType: Int = 100
        override val interval: Int = 7

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Week

        override fun dates(mapper: HabitDateMapper, timesCount: Int, typeCount: Int) =
            mapper.mapCustomWeek(timesCount, typeCount)
    }

    data object Month : FrequencyCustomType() {
        override val title: Int = R.plurals.month_label
        override val maxTimes: Int = 31
        override val maxType: Int = 12

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Month

        override fun dates(mapper: HabitDateMapper, timesCount: Int, typeCount: Int) =
            mapper.mapCustomMonth(timesCount, typeCount)
    }
}