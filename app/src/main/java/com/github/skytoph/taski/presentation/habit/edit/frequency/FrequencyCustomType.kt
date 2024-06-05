package com.github.skytoph.taski.presentation.habit.edit.frequency

import androidx.annotation.PluralsRes
import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.Frequency
import com.github.skytoph.taski.presentation.habit.create.GoalState

sealed class FrequencyCustomType {
    @get:PluralsRes
    abstract val title: Int

    abstract val maxTimes: Int
    abstract val maxType: Int

    fun times(times: Int, type: Int) =
        GoalState(times, canBeIncreased = (times < maxTimes * type), canBeDecreased = times > 1)

    fun type(type: Int) =
        GoalState(value = type, canBeIncreased = (type < maxType), canBeDecreased = type > 1)

    abstract fun map(): Frequency.Custom.Type

    data object Day : FrequencyCustomType() {
        override val title: Int = R.plurals.day_label
        override val maxTimes: Int = 1
        override val maxType: Int = 365

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Day
    }

    data object Week : FrequencyCustomType() {
        override val title: Int = R.plurals.week_label
        override val maxTimes: Int = 7
        override val maxType: Int = 100

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Week
    }

    data object Month : FrequencyCustomType() {
        override val title: Int = R.plurals.month_label
        override val maxTimes: Int = 31
        override val maxType: Int = 12

        override fun map(): Frequency.Custom.Type = Frequency.Custom.Type.Month
    }
}