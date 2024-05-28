package com.github.skytoph.taski.presentation.habit.edit.frequency

import androidx.annotation.PluralsRes
import com.github.skytoph.taski.R

sealed interface FrequencyCustomType {
    @get:PluralsRes
    val title: Int

    object Day : FrequencyCustomType {
        override val title: Int = R.plurals.day_label
    }

    object Week : FrequencyCustomType {
        override val title: Int = R.plurals.week_label
    }

    object Month : FrequencyCustomType {
        override val title: Int = R.plurals.month_label
    }
}