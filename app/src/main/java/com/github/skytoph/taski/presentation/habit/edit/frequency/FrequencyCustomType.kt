package com.github.skytoph.taski.presentation.habit.edit.frequency

import androidx.annotation.PluralsRes
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.create.GoalState

sealed interface FrequencyCustomType {
    @get:PluralsRes
    val title: Int

    val maxTimes: Int
    val maxType: Int

    fun times(times: Int, type: Int) =
        GoalState(times, canBeIncreased = times < maxTimes * type, canBeDecreased = times > 1)

    fun type(times: Int, type: Int) =
        GoalState(value = type, canBeIncreased = type < 365, canBeDecreased = type > 1)

    object Day : FrequencyCustomType {
        override val title: Int = R.plurals.day_label
        override val maxTimes: Int = 1
        override val maxType: Int = 365
    }

    object Week : FrequencyCustomType {
        override val title: Int = R.plurals.week_label
        override val maxTimes: Int = 7
        override val maxType: Int = 100
    }

    object Month : FrequencyCustomType {
        override val title: Int = R.plurals.month_label
        override val maxTimes: Int = 31
        override val maxType: Int = 12
    }
}