package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.core.datastore.settings.ChartFrequency
import com.skytoph.taski.presentation.habit.details.ChartFrequencyUi

fun ChartFrequency?.map(): ChartFrequencyUi = when (this) {
    ChartFrequency.Day -> ChartFrequencyUi.Day
    ChartFrequency.Week -> ChartFrequencyUi.Week
    ChartFrequency.Month -> ChartFrequencyUi.Month
    ChartFrequency.Year -> ChartFrequencyUi.Year
    else -> ChartFrequencyUi.Day
}

fun ChartFrequencyUi.map(): ChartFrequency = when (this) {
    ChartFrequencyUi.Day -> ChartFrequency.Day
    ChartFrequencyUi.Week -> ChartFrequency.Week
    ChartFrequencyUi.Month -> ChartFrequency.Month
    ChartFrequencyUi.Year -> ChartFrequency.Year
}