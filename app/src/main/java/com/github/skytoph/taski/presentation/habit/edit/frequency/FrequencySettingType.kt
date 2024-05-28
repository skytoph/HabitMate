package com.github.skytoph.taski.presentation.habit.edit.frequency

sealed interface FrequencySettingType {
    object Daily : FrequencySettingType
    object Monthly : FrequencySettingType
    object Custom : FrequencySettingType
}