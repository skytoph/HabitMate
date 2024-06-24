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
                    val timesDone = current.second.count { it.value.isCompleted(goal) }
                    streaks.apply {
                        when {
                            prev.first == 0 -> {
                                streaks[lastIndex] += prev.second.count { it.value.isCompleted(goal) }
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

    class Day(timesCount: Int, private val typeCount: Int) : CalculateStreak.Abstract() {

        private val skipMax = typeCount - timesCount

        override fun streaks(
            data: Map<Int, Entry>, goal: Int, days: List<Int>, findCurrentStreak: Boolean
        ): List<Int> =
            if (data.isEmpty() || findCurrentStreak && data.iterator().next().key > skipMax)
                emptyList()
            else {
                val dataReversed = data.toList().reversed()
                val streaks = mutableListOf<Int>()
                val firstEntry = dataReversed.iterator().next()
                var skip = 0
                var nextSkipReset = firstEntry.first - typeCount + 1
                if (firstEntry.second.isCompleted(goal)) streaks[streaks.lastIndex]++

                dataReversed.toList()
                    .zipWithNext()
                    .fold(streaks) { streaks, (prev, current) ->
                        val daysAgo = current.first
                        if (daysAgo < nextSkipReset)
                            if (skip + prev.first - nextSkipReset > skipMax
                                || prev.first - daysAgo - 1 > skipMax
                            ) {
                                if (streaks[streaks.lastIndex] > 0) streaks.add(0)
                                skip = 0
                                nextSkipReset = daysAgo - typeCount - 1
                            } else {
                                skip = nextSkipReset - daysAgo - 1
                                nextSkipReset -= typeCount
                            }
                        else
                            skip += prev.first - daysAgo - 1
                        streaks.apply {
                            when {
                                skip <= skipMax ->
                                    if (current.second.isCompleted(goal)) streaks[lastIndex]++
                                    else skip++

                                current.second.isCompleted(goal) -> add(1)
                                streaks[streaks.lastIndex] > 0 -> add(0)
                            }
                        }
                    }.let { list ->
                        if (findCurrentStreak) listOf(list.last()) else list
                    }
            }
    }
}