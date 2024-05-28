package com.github.skytoph.taski.presentation.habit.edit.frequency

data class FrequencyState(
    val timesCount: Int = 7,
    val inCount: Int = 2,
    val frequencyType: FrequencyCustomType = FrequencyCustomType.Week,
)