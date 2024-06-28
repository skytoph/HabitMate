package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.HabitStatistics
import com.github.skytoph.taski.presentation.habit.details.Streak

interface CalculateStreak {
    fun streaks(data: Map<Int, Entry>, goal: Int): HabitStatistics

    abstract class Base : CalculateStreak {

        protected abstract val skipMax: Int
        override fun streaks(data: Map<Int, Entry>, goal: Int): HabitStatistics =
            streaksList(data = data, goal = goal).let { list ->
                if (list.isEmpty()) HabitStatistics()
                else HabitStatistics(
                    currentStreak = if (isStreakCurrently(data, goal)) currentStreak(list) else 0,
                    bestStreak = list.maxBy { it.streak }.streak,
                    total = data.count { it.value.isCompleted(goal) },
                    streaks = list
                )
            }

        abstract fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak>

        abstract fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean

        open fun currentStreak(list: List<Streak>): Int = list.first().streak
    }

    abstract class Abstract(private val days: Set<Int> = emptySet()) : Base() {
        override fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak> =
            streaks(data = data, goal = goal, days = days.reversed())

        protected abstract fun streaks(data: Map<Int, Entry>, goal: Int, days: List<Int>)
                : List<Streak>

        override fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean {
            val iterator = data.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.key > skipMax) return false
                if (item.value.isCompleted(goal)) return true
            }
            return false
        }
    }

    abstract class Iterable(
        private val days: Set<Int> = emptySet(),
        private val counter: StreakCounterCache = StreakCounterCache(),
    ) : Abstract(days), StartAndEnd {

        override fun streaks(data: Map<Int, Entry>, goal: Int, days: List<Int>): List<Streak> =
            if (data.isEmpty() || days.isEmpty()) emptyList()
            else {
                val streaks: MutableList<Streak> = mutableListOf()

                val dataIterator = data.keys.iterator()
                var daysIterator = days.listIterator()

                val today = dayNumber(0)
                var dayNextValue = daysIterator.next()
                    .also { if (!daysIterator.hasNext()) daysIterator = days.listIterator() }
                var dayCurrentValue = dayNextValue
                while (dayNextValue > today && daysIterator.hasNext()) {
                    dayNextValue = daysIterator.next()
                }
                if (!daysIterator.hasNext() && dayNextValue > today) {
                    daysIterator = days.listIterator()
                    dayNextValue = daysIterator.next()
                }
                var currentStreak = days.indexOf(dayNextValue)

                var dayNextPosition = findNextPosition(-1, dayNextValue)
                var dataNextPosition = dataIterator.next()

                var daysAgo: Int
                while (dataNextPosition > BREAK_VALUE) {
                    daysAgo = when {
                        dayNextPosition > dataNextPosition -> dataNextPosition.also {
                            dataNextPosition =
                                if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }

                        dayNextPosition < dataNextPosition -> dayNextPosition.also {
                            if (!daysIterator.hasNext()) {
                                daysIterator = days.listIterator()
                            }
                            dayCurrentValue = dayNextValue
                            dayNextValue = daysIterator.next()
                            dayNextPosition = findNextPosition(dayNextPosition, dayNextValue)
                        }

                        else -> dayNextPosition.also {
                            if (!daysIterator.hasNext()) {
                                daysIterator = days.listIterator()
                            }
                            dayCurrentValue = dayNextValue
                            dayNextValue = daysIterator.next()
                            dayNextPosition = findNextPosition(dayNextPosition, dayNextValue)
                            dataNextPosition =
                                if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }
                    }

                    val entry = data[daysAgo]

                    when {
                        entry == null || !entry.isCompleted(goal) -> {
                            counter.save(list = streaks)
                            currentStreak = 0
                        }

                        else -> {
                            val isPointedToGoal = dayNumber(daysAgo) == dayCurrentValue
                            if (isPointedToGoal) currentStreak++
                            if (isPointedToGoal && currentStreak == days.size) {
                                val start = end(daysAgo)
                                val end = start(daysAgo)
                                counter.add(count = 1, start = end)
                                counter.setStart(start = start)
                                currentStreak = 0
                            } else
                                counter.add(count = 1, start = daysAgo)
                        }
                    }
                }
                streaks.apply { counter.save(list = this) }
            }

        open fun findNextPosition(currentPosition: Int, nextValue: Int): Int {
            var startPosition = currentPosition
            var result = currentPosition
            while (result == currentPosition)
                result = findPosition(startPosition++, nextValue)
            return result
        }

        open fun findPosition(currentPosition: Int, nextValue: Int): Int {
            val currentValue = dayNumber(currentPosition)
            return currentPosition + currentValue - when {
                currentValue < nextValue ->
                    nextValue - dayNumber(currentPosition + currentValue)

                else -> nextValue
            }
        }

        abstract fun dayNumber(daysAgo: Int): Int

        abstract val maxDays: Int

        override val skipMax: Int
            get() {
                val iterator = days.reversed().iterator()
                val today = dayNumber(0)
                var day = iterator.next()
                while (iterator.hasNext() && day > today)
                    day = iterator.next()
                return when {
                    day == today -> 0
                    day < today -> today - day
                    else -> maxDays + today - days.reversed().iterator().next()
                }
            }

        companion object {
            private const val BREAK_VALUE = -1
        }
    }
}
