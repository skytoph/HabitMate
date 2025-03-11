package com.skytoph.taski.presentation.habit.details.streak

import com.skytoph.taski.core.LoopIterator
import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.presentation.habit.details.HabitState
import com.skytoph.taski.presentation.habit.details.HabitStatistics
import com.skytoph.taski.presentation.habit.details.Streak

interface CalculateStreak {
    fun streaks(data: Map<Int, Entry>, goal: Int): HabitStatistics
    fun currentState(data: Map<Int, Entry>, goal: Int): HabitState

    abstract class Base : CalculateStreak {

        override fun streaks(data: Map<Int, Entry>, goal: Int): HabitStatistics =
            streaksList(data = data, goal = goal).let { list ->
                if (list.isEmpty()) HabitStatistics()
                else HabitStatistics(
                    currentStreak = if (isStreakCurrently(list)) currentStreak(list) else 0,
                    bestStreak = list.maxBy { it.streak }.streak,
                    total = data.count { it.value.isCompleted(goal) },
                    streaks = list,
                )
            }

        override fun currentState(data: Map<Int, Entry>, goal: Int): HabitState = HabitState(
            isStreakCurrently = isStreakCurrently(data, goal),
            isScheduledForToday = isScheduledForToday(data, goal)
        )

        abstract fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak>

        private fun currentStreak(list: List<Streak>): Int = list.first().streak

        abstract fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean

        protected fun isStreakCurrently(list: List<Streak>): Boolean = list.isNotEmpty() && list.first().start <= 1

        abstract fun isScheduledForToday(data: Map<Int, Entry>, goal: Int): Boolean

        protected fun isScheduledForToday(list: List<Streak>): Boolean = list.isEmpty() || list.first().start > 0
    }

    abstract class Abstract(private val days: Set<Int> = emptySet()) : Base() {
        protected abstract val skipMax: Int

        override fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak> =
            streaks(data = data, goal = goal, days = days.reversed())

        protected abstract fun streaks(data: Map<Int, Entry>, goal: Int, days: List<Int>): List<Streak>

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
    ) : Abstract(days), StartAndEnd, TransformWeekDay {

        override fun streaks(data: Map<Int, Entry>, goal: Int, days: List<Int>): List<Streak> =
            if (data.isEmpty() || days.isEmpty()) emptyList()
            else {
                val streaks: MutableList<Streak> = mutableListOf()

                val dataIterator = data.keys.iterator()
                val daysIterator = LoopIterator(days)

                val today = transform(dayNumber(0))
                var dayNextValue = daysIterator.next()
                while (transform(dayNextValue) > today && daysIterator.hasNext()) {
                    dayNextValue = daysIterator.next()
                }
                if (!daysIterator.hasNext() && transform(dayNextValue) > today) {
                    dayNextValue = daysIterator.next()
                }
                val currentStreak = mutableListOf(days.indexOf(dayNextValue))

                var dayNextPosition = findNextPosition(-1, dayNextValue)
                var dataNextPosition = dataIterator.next()

                var daysAgo: Int
                while (dataNextPosition > BREAK_VALUE) {
                    daysAgo = when {
                        dayNextPosition > dataNextPosition -> dataNextPosition.also {
                            dataNextPosition = if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }

                        dayNextPosition < dataNextPosition -> dayNextPosition.also {
                            dayNextValue = daysIterator.next()
                            dayNextPosition = findNextPosition(dayNextPosition, dayNextValue)
                        }

                        else -> dayNextPosition.also {
                            dayNextValue = daysIterator.next()
                            dayNextPosition = findNextPosition(dayNextPosition, dayNextValue)
                            dataNextPosition = if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }
                    }

                    val entry = data[daysAgo]

                    when {
                        entry == null || !entry.isCompleted(goal) -> {
                            counter.save(list = streaks)
                            currentStreak[0] = 0
                        }

                        else -> {
                            val dayNumber = dayNumber(daysAgo)
                            val isPointedToGoal = dayNumber == daysIterator.previousAndReturnBack(2)
                            if (isPointedToGoal) {
                                currentStreak[0]++
                                val next = daysIterator.previousAndReturnBack()
                                val prev = daysIterator.previousAndReturnBack(3)
                                val end = end(daysAgo)
                                var positionNext = 0
                                var positionPrev = 0
                                when {
                                    dayNumber == days.first() && dayNumber == days.last() -> {
                                        positionNext = findPosition(start(daysAgo) + 1, next) - 1
                                        positionPrev = findPosition(end(end - 1), prev) + 1
                                        if (end < positionPrev) positionPrev = end
                                    }

                                    dayNumber == days.first() -> {
                                        positionNext = findPosition(end, next) - 1
                                        positionPrev = findPosition(end(end - 1), prev) + 1
                                        if (end < positionPrev) positionPrev = end
                                    }

                                    dayNumber == days.last() -> {
                                        positionNext = findPosition(start(daysAgo) + 1, next) - 1
                                        positionPrev = findPosition(end, prev) + 1
                                    }

                                    else -> {
                                        positionNext = findPosition(end, next) - 1
                                        positionPrev = findPosition(end, prev) + 1
                                    }
                                }
                                counter.add(count = 1, start = positionPrev, end = positionNext)
                            } else {
                                counter.add(count = 1)
                            }
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
            val next = transform(nextValue)
            val currentValue = transform(dayNumber(currentPosition))
            return currentPosition + currentValue - when {
                currentValue < next ->
                    next - transform(dayNumber(currentPosition + currentValue))

                else -> next
            }
        }

        abstract fun dayNumber(daysAgo: Int): Int

        abstract val maxDays: Int

        override val skipMax: Int
            get() {
                val iterator = days.reversed().iterator()
                val today = transform(dayNumber(0))
                var day = transform(iterator.next())
                while (iterator.hasNext() && day >= today)
                    day = transform(iterator.next())
                return when {
                    day < today -> today - day
                    else -> maxDays + today - transform(days.reversed().iterator().next())
                }
            }

        override fun isScheduledForToday(data: Map<Int, Entry>, goal: Int): Boolean = days.contains(dayNumber(0))

        companion object {
            private const val BREAK_VALUE = -1
        }
    }
}