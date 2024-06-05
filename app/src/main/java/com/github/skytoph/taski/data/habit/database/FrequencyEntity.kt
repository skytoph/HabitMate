package com.github.skytoph.taski.data.habit.database

import com.github.skytoph.taski.domain.habit.Frequency

sealed class FrequencyEntity {
    abstract fun map(): Frequency

    data class Daily(val days: Set<Int>) : FrequencyEntity() {
        override fun map(): Frequency = Frequency.Daily(days)
    }

    data class Monthly(val days: Set<Int>) : FrequencyEntity() {
        override fun map(): Frequency = Frequency.Monthly(days)
    }

    data class Custom(
        val timesCount: Int,
        val typeCount: Int,
        val frequencyType: String
    ) : FrequencyEntity() {
        override fun map(): Frequency =
            Frequency.Custom(timesCount, typeCount, Frequency.Custom.Type.getType(frequencyType))
    }
}