package com.skytoph.taski.data.habit.database

import androidx.annotation.Keep
import com.skytoph.taski.domain.habit.Frequency

@Keep
sealed class FrequencyEntity {
    abstract fun map(): Frequency

    @Keep
    data class Daily(val days: Set<Int>) : FrequencyEntity() {
        override fun map(): Frequency = Frequency.Daily(days)
    }

    @Keep
    data class Monthly(val days: Set<Int>) : FrequencyEntity() {
        override fun map(): Frequency = Frequency.Monthly(days)
    }

    @Keep
    data class Custom(
        val timesCount: Int,
        val typeCount: Int,
        val frequencyType: String
    ) : FrequencyEntity() {
        override fun map(): Frequency =
            Frequency.Custom(timesCount, typeCount, Frequency.Custom.Type.getType(frequencyType))
    }
}