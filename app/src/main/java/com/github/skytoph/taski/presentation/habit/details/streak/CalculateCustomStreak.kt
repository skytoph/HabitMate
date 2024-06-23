package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry

abstract class CalculateCustomStreak(
    private val timesCount: Int,
    private val typeCount: Int,
) : CalculateStreak.Abstract() {

    abstract fun divide(daysAgo: Int): Int

    override fun streaks(
        data: Map<Int, Entry>, goal: Int, days: List<Int>, findCurrentStreak: Boolean
    ): List<Int> =
        if (data.isEmpty()) listOf(0)
        else {
            val frequencyGoal = timesCount * typeCount
            data.asSequence()
                .toList()
                .groupBy { divide(it.key) }.toList().zipWithNext()
                .fold(mutableListOf(0)) { streaks, (prev, current) ->
                    val timesDone = current.second.count { it.value.timesDone >= goal }
                    streaks.apply {
                        when {
                            prev.first == 0 -> {
                                streaks[lastIndex] += prev.second.count { it.value.timesDone >= goal }
                                if (current.first == 1 && timesDone >= frequencyGoal) streaks[lastIndex] += timesDone
                                else if (findCurrentStreak) return streaks
                                else add(timesDone)
                            }

                            current.first == prev.first + 1 && timesDone >= frequencyGoal -> streaks[lastIndex] += timesDone
                            findCurrentStreak -> return streaks
                            else -> add(timesDone)
                        }
                    }
                }
        }

    class Week(timesCount: Int, private val typeCount: Int, private val now: Now) :
        CalculateCustomStreak(timesCount, typeCount) {

        override fun divide(daysAgo: Int): Int =
            (daysAgo + now.dayOfWeek(daysAgo) - 1) / (7 * typeCount)
    }

    class Month(timesCount: Int, private val typeCount: Int, private val now: Now) :
        CalculateCustomStreak(timesCount, typeCount) {

        override fun divide(daysAgo: Int): Int = now.monthsAgo(daysAgo) / typeCount
    }

    class Day(timesCount: Int, private val typeCount: Int) :
        CalculateCustomStreak(timesCount, 1) {

        override fun divide(daysAgo: Int): Int = daysAgo / typeCount
    }
}