package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.data.habit.database.FrequencyEntity
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.details.mapper.FrequencyMapper
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi

sealed interface Frequency {
    fun mapToUi(): FrequencyUi
    fun mapToDB(): FrequencyEntity
    fun map(mapper: FrequencyMapper): HabitStatisticsUi
    fun isEveryday(): Boolean

    data class Daily(val days: Set<Int> = (1..7).toSet()) : Frequency {
        override fun mapToUi(): FrequencyUi = FrequencyUi.Daily(days)
        override fun mapToDB(): FrequencyEntity = FrequencyEntity.Daily(days)
        override fun map(mapper: FrequencyMapper): HabitStatisticsUi = mapper.map(days)
        override fun isEveryday(): Boolean = days.size >= 7
    }

    data class Monthly(val days: Set<Int>) : Frequency {
        override fun mapToUi(): FrequencyUi = FrequencyUi.Monthly(days)
        override fun mapToDB(): FrequencyEntity = FrequencyEntity.Monthly(days)
        override fun map(mapper: FrequencyMapper): HabitStatisticsUi = mapper.map(days)
        override fun isEveryday(): Boolean = days.size >= 31
    }

    data class Custom(
        val timesCount: Int,
        val typeCount: Int,
        val type: Type
    ) : Frequency {

        override fun mapToUi(): FrequencyUi {
            val typeUi = type.map()
            return FrequencyUi.Custom(
                timesCount = typeUi.times(timesCount, typeCount),
                typeCount = typeUi.type(typeCount),
                frequencyType = typeUi
            )
        }

        override fun mapToDB(): FrequencyEntity =
            FrequencyEntity.Custom(timesCount, typeCount, type.name)

        override fun map(mapper: FrequencyMapper): HabitStatisticsUi = mapper.map()

        override fun isEveryday(): Boolean = false

        sealed interface Type {
            fun map(): FrequencyCustomType
            val name: String

            data object Day : Type {
                override val name: String = "day"
                override fun map(): FrequencyCustomType = FrequencyCustomType.Day
            }

            data object Week : Type {
                override val name: String = "week"
                override fun map(): FrequencyCustomType = FrequencyCustomType.Week
            }

            data object Month : Type {
                override val name: String = "month"
                override fun map(): FrequencyCustomType = FrequencyCustomType.Month
            }

            companion object {
                private val allTypes =
                    mapOf(Day.name to Day, Week.name to Week, Month.name to Month)

                fun getType(type: String) = allTypes[type] ?: Day
            }
        }
    }
}