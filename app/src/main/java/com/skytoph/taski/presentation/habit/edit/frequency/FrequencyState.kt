package com.skytoph.taski.presentation.habit.edit.frequency

data class FrequencyState(
    val daily: FrequencyUi = FrequencyUi.Everyday(FrequencyUi.Daily()),
    val monthly: FrequencyUi = FrequencyUi.Monthly(),
    val custom: FrequencyUi = FrequencyUi.Custom(),
    val selectedName: String = daily.name
) {
    private val map: Map<String, FrequencyUi>
        get() = mapOf(daily.name to daily, monthly.name to monthly, custom.name to custom)

    val selected: FrequencyUi
        get() = map.getOrDefault(selectedName, daily)

    fun isSelected(frequency: FrequencyUi): Boolean = selectedName == frequency.name

    fun updateSelected(updated: FrequencyUi): FrequencyState = when (selectedName) {
        daily.name -> copy(daily = updated)
        monthly.name -> copy(monthly = updated)
        else -> copy(custom = updated)
    }

    fun updateCustom(typeCustom: FrequencyCustomType): FrequencyState = when (custom) {
        is FrequencyUi.Custom -> {
            val type = typeCustom.type(custom.typeCount.value)
            val times = typeCustom.times(times = custom.timesCount.value, type = type.value)
            val updated =
                FrequencyUi.Custom(frequencyType = typeCustom, timesCount = times, typeCount = type)
            copy(custom = updated)
        }

        is FrequencyUi.Everyday -> copy(custom = custom.frequency).updateCustom(typeCustom)
        else -> this
    }
}