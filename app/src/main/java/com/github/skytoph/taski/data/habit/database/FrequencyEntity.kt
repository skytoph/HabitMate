package com.github.skytoph.taski.data.habit.database

sealed class FrequencyEntity {

    data class Daily(val days: List<Int> = (1..7).toList()) : FrequencyEntity()

    data class Monthly(val days: List<Int>) : FrequencyEntity()

    data class Custom(
        val timesCount: Int,
        val typeCount: Int,
        val frequencyType: String
    ) : FrequencyEntity()
}