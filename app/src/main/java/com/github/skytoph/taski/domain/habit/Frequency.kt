package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.data.habit.database.FrequencyEntity
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.details.mapper.CalculatorProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.FrequencyMapper
import com.github.skytoph.taski.presentation.habit.details.streak.CalculateStreak
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi

sealed interface Frequency {
    fun mapToUi(): FrequencyUi
    fun mapToDB(): FrequencyEntity
    fun map(mapper: FrequencyMapper): HabitStatisticsUi
    fun isEveryday(): Boolean
    fun provide(provider: CalculatorProvider): CalculateStreak
    val times: Int

    data class Daily(val days: Set<Int> = (1..7).toSet()) : Frequency {
        override fun mapToUi(): FrequencyUi = FrequencyUi.Daily(days)
        override fun mapToDB(): FrequencyEntity = FrequencyEntity.Daily(days)
        override fun map(mapper: FrequencyMapper): HabitStatisticsUi = mapper.map(days)
        override fun isEveryday(): Boolean = days.size >= 7
        override fun provide(provider: CalculatorProvider): CalculateStreak =
            provider.provideDaily(isEveryday(), days)

        override val times: Int = days.size.let { if (isEveryday()) 1 else it }
    }

    data class Monthly(val days: Set<Int>) : Frequency {
        override fun mapToUi(): FrequencyUi = FrequencyUi.Monthly(days)
        override fun mapToDB(): FrequencyEntity = FrequencyEntity.Monthly(days)
        override fun map(mapper: FrequencyMapper): HabitStatisticsUi = mapper.map(days)
        override fun isEveryday(): Boolean = days.size >= 31
        override fun provide(provider: CalculatorProvider): CalculateStreak =
            provider.provideMonthly(isEveryday(), days)

        override val times: Int = days.size.let { if (isEveryday()) 1 else it }
    }

    data class Custom(
        val timesCount: Int,
        val typeCount: Int,
        val type: Type
    ) : Frequency {

        override val times: Int = timesCount

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

        override fun isEveryday(): Boolean = type.isEveryday(timesCount, typeCount)

        override fun provide(provider: CalculatorProvider): CalculateStreak =
            type.provide(provider, timesCount, typeCount)

        sealed interface Type {
            fun map(): FrequencyCustomType
            fun provide(provider: CalculatorProvider, times: Int, type: Int): CalculateStreak
            fun isEveryday(timesCount: Int, typeCount: Int): Boolean

            val name: String

            data object Day : Type {
                override val name: String = "day"
                override fun map(): FrequencyCustomType = FrequencyCustomType.Day
                override fun isEveryday(timesCount: Int, typeCount: Int): Boolean =
                    timesCount == typeCount

                override fun provide(provider: CalculatorProvider, times: Int, type: Int) =
                    provider.provideCustomDaily(isEveryday(times, type), times, type)
            }

            data object Week : Type {
                override val name: String = "week"
                override fun map(): FrequencyCustomType = FrequencyCustomType.Week
                override fun isEveryday(timesCount: Int, typeCount: Int): Boolean =
                    timesCount == 7 * typeCount

                override fun provide(provider: CalculatorProvider, times: Int, type: Int) =
                    provider.provideCustomWeekly(isEveryday(times, type), times, type)
            }

            data object Month : Type {
                override val name: String = "month"
                override fun map(): FrequencyCustomType = FrequencyCustomType.Month
                override fun isEveryday(timesCount: Int, typeCount: Int): Boolean =
                    timesCount == 31 * typeCount

                override fun provide(provider: CalculatorProvider, times: Int, type: Int) =
                    provider.provideCustomMonthly(isEveryday(times, type), times, type)
            }

            companion object {
                private val allTypes =
                    mapOf(Day.name to Day, Week.name to Week, Month.name to Month)

                fun getType(type: String) = allTypes[type] ?: Day
            }
        }
    }
}