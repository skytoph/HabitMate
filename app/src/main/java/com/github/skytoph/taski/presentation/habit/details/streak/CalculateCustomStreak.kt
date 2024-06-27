package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.Streak

abstract class CalculateCustomStreak(
    timesCount: Int,
    typeCount: Int,
    private val counter: StreakCounterCache = StreakCounterCache(),
) : CalculateStreak.Abstract() {

    private val frequencyGoal = timesCount * typeCount

    abstract fun divide(daysAgo: Int): Int

    abstract fun end(index: Int): Int
    abstract fun start(index: Int): Int

    override fun streaks(data: Map<Int, Entry>, goal: Int, days: List<Int>): List<Streak> =
        if (data.isEmpty()) emptyList()
        else {
            val streaks = mutableListOf<Streak>()
            data.asSequence()
                .toList()
                .groupBy { divide(it.key) }
                .toList()
                .also { items ->
                    val firstItem = items.firstOrNull() ?: return@also
                    val timesDone = firstItem.second.count { it.value.isCompleted(goal) }
                    addCounter(timesDone, goal, firstItem.first, firstItem.second)
                    if (timesDone < frequencyGoal) counter.save(streaks)
                }
                .zipWithNext()
                .fold(streaks) { list, (prev, current) ->
                    val position = current.first
                    val timesDone = current.second.count { it.value.isCompleted(goal) }

                    if (position != prev.first + 1 || timesDone < frequencyGoal)
                        counter.save(list = list)
                    addCounter(timesDone, goal, position, current.second)

                    list
                }.apply { counter.save(this) }
        }

    private fun addCounter(
        timesDone: Int, goal: Int, position: Int, entries: List<Map.Entry<Int, Entry>>
    ) = if (timesDone < frequencyGoal)
        counter.add(
            count = timesDone,
            start = entries.firstOrNull { it.value.isCompleted(goal) }?.key,
            end = entries.lastOrNull { it.value.isCompleted(goal) }?.key
        )
    else counter.add(count = timesDone, start = end(position), end = start(position))

    class Week(timesCount: Int, private val typeCount: Int, private val now: Now) :
        CalculateCustomStreak(timesCount = timesCount, typeCount = typeCount) {

        override val skipMax: Int = now.dayOfWeek() + 7 * (typeCount - 1)

        override fun divide(daysAgo: Int): Int =
            (daysAgo + now.dayOfWeek(daysAgo) - 1) / (7 * typeCount)

        override fun start(index: Int): Int = now.firstDayOfWeekDaysAgo(index)

        override fun end(index: Int): Int = now.lastDayOfWeekDaysAgo(index)
    }

    class Month(timesCount: Int, private val typeCount: Int, private val now: Now) :
        CalculateCustomStreak(timesCount = timesCount, typeCount = typeCount) {

        override val skipMax: Int = now.dayOfMonths(0) +
                if (typeCount > 1) (1..<typeCount).sumOf { now.daysInMonth(it) } else 0

        override fun divide(daysAgo: Int): Int = now.monthsAgo(daysAgo) / typeCount

        override fun start(index: Int): Int = now.firstDayOfMonthDaysAgo(index)

        override fun end(index: Int): Int = now.lastDayOfMonthDaysAgo(index)
    }

    class Day(
        timesCount: Int,
        private val typeCount: Int,
        private val counter: StreakCounterCache = StreakCounterCache(),
    ) : CalculateStreak.Abstract() {

        override val skipMax: Int = typeCount - timesCount

        override fun streaks(data: Map<Int, Entry>, goal: Int, days: List<Int>): List<Streak> =
            if (data.isEmpty()) emptyList()
            else {
                val dataReversed = data.toList().reversed()
                val firstEntry = dataReversed.iterator().next()
                var nextSkipReset = firstEntry.first - typeCount
                if (firstEntry.second.isCompleted(goal))
                    counter.add(count = 1, start = firstEntry.first)
                var skip = 0

                dataReversed.toList()
                    .zipWithNext()
                    .fold(mutableListOf<Streak>()) { streaks, (prev, current) ->
                        val daysAgo = current.first
                        if (daysAgo <= nextSkipReset) {
                            val beforeReset = skip + prev.first - nextSkipReset - 1
                            val afterReset = nextSkipReset - daysAgo
                            if (beforeReset > skipMax || afterReset > skipMax) {
                                counter.save(list = streaks, addCount = 0)
                                skip = 0
                                nextSkipReset = daysAgo - typeCount
                            } else {
                                skip = afterReset
                                nextSkipReset -= typeCount
                            }
                        } else
                            skip += prev.first - daysAgo - 1
                        when {
                            skip <= skipMax ->
                                if (current.second.isCompleted(goal))
                                    counter.add(count = 1, start = daysAgo)
                                else skip++

                            current.second.isCompleted(goal) -> {
                                counter.save(list = streaks, addCount = skipMax - skip)
                                counter.add(count = 1, start = daysAgo)
                            }

                            else -> counter.save(list = streaks, addCount = skipMax - skip)
                        }
                        streaks
                    }.apply { counter.save(list = this, addCount = skipMax - skip) }.reversed()
            }
    }
}