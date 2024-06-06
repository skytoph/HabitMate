package com.github.skytoph.taski.presentation.habit.edit.frequency

data class FrequencyState(
    val daily: FrequencyUi = FrequencyUi.Daily(),
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
}