package com.skytoph.taski.presentation.habit.details.streak

import com.skytoph.taski.core.Now
import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.presentation.habit.details.HabitState
import com.skytoph.taski.presentation.habit.details.Streak

abstract class CalculateCustomStreak(
    private val timesCount: Int,
    private val typeCount: Int,
    private val counter: StreakCounterCache = StreakCounterCache(),
) : CalculateStreak.Base(), StartAndEnd {

    override fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak> =
        if (data.isEmpty()) emptyList()
        else {
            val dataReversed = data.toList().reversed()
            val firstEntry = dataReversed.iterator().next()
            var nextSkipReset = nextReset(firstEntry.first)
            var streak = 0
            if (firstEntry.second.isCompleted(goal)) {
                counter.add(count = 1, start = start(firstEntry.first), end = firstEntry.first)
                streak++
            }
            var skipLimit = skipMax(firstEntry.first)
            var skip = skipInitial(firstEntry.first)

            dataReversed.toList()
                .zipWithNext()
                .fold(mutableListOf<Streak>()) { streaks, (prev, current) ->
                    val daysAgo = current.first
                    if (daysAgo <= nextSkipReset) {
                        val beforeReset = prev.first - nextSkipReset - 1
                        val afterReset = nextSkipReset - daysAgo
                        if (skip + beforeReset > skipLimit || afterReset > skipLimit) {
                            counter.save(
                                list = streaks,
                                addCount = if (skip + beforeReset > skipLimit) 0 else beforeReset
                            )
                            skip = skipInitial(daysAgo)
                            nextSkipReset = nextReset(daysAgo)
                        } else {
                            skip = afterReset
                            nextSkipReset = nextReset(nextSkipReset)
                        }
                        skipLimit = skipMax(daysAgo)
                        streak = 0
                    } else
                        skip += prev.first - daysAgo - 1
                    when {
                        current.second.isCompleted(goal) -> {
                            if (streak == 0) counter.add(count = 1, start = start(daysAgo), end = daysAgo)
                            else counter.add(count = 1, start = daysAgo)
                            streak++
                        }

                        else -> skip++
                    }
                    streaks
                }.apply {
                    counter.save(
                        list = this,
                        addCount = if (streak < timesCount) 0 else dataReversed.last().first - nextSkipReset - 1
                    )
                }
                .reversed()
        }

    override fun currentState(data: Map<Int, Entry>, goal: Int): HabitState =
        streaksList(data = dataWithoutToday(data), goal = goal).let { streaks ->
            HabitState(
                isStreakCurrently = isStreakCurrently(streaks),
                isScheduledForToday = isScheduledForToday(streaks)
            )
        }

    private fun dataWithoutToday(data: Map<Int, Entry>): Map<Int, Entry> = data.toMutableMap().apply { remove(0) }

    override fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean =
        isStreakCurrently(streaksList(dataWithoutToday(data), goal))

    override fun isScheduledForToday(data: Map<Int, Entry>, goal: Int): Boolean =
        isScheduledForToday(streaksList(dataWithoutToday(data), goal))

    abstract fun nextReset(daysAgo: Int): Int
    abstract fun skipMax(start: Int): Int
    abstract fun skipInitial(daysAgo: Int): Int

    class Month(
        private val timesCount: Int,
        private val typeCount: Int,
        private val now: Now,
    ) : CalculateCustomStreak(timesCount = timesCount, typeCount = typeCount),
        CalculateInterval by CalculateInterval.Month(now = now, offset = typeCount - 1) {

        override fun nextReset(daysAgo: Int): Int = end(daysAgo) - 1
        override fun skipMax(daysAgo: Int): Int = start(daysAgo) - end(daysAgo) - timesCount + 1
        override fun skipInitial(daysAgo: Int): Int = now.dayOfMonths(daysAgo) - 1
    }

    class Week(
        private val timesCount: Int,
        private val typeCount: Int,
        private val now: Now,
    ) : CalculateCustomStreak(timesCount = timesCount, typeCount = typeCount),
        TransformWeekDay by TransformWeekDay.Base(now),
        CalculateInterval by CalculateInterval.Week(now = now, offset = typeCount - 1) {

        override fun nextReset(daysAgo: Int): Int = end(daysAgo) - 1
        override fun skipMax(start: Int): Int = 7 * typeCount - timesCount
        override fun skipInitial(daysAgo: Int): Int = now.dayOfWeek(now.default, daysAgo) - 1
    }

    class Day(
        private val timesCount: Int,
        private val typeCount: Int,
    ) : CalculateCustomStreak(timesCount = timesCount, typeCount = typeCount) {

        override fun start(index: Int): Int = index
        override fun end(index: Int): Int = index
        override fun nextReset(daysAgo: Int): Int = daysAgo - typeCount
        override fun skipMax(start: Int): Int = typeCount - timesCount
        override fun skipInitial(daysAgo: Int): Int = 0
    }
}