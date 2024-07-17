package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.HabitState
import com.github.skytoph.taski.presentation.habit.details.HabitStatistics
import com.github.skytoph.taski.presentation.habit.details.Streak

interface CalculateStreak {
    fun streaks(data: Map<Int, Entry>, goal: Int): HabitStatistics
    fun currentState(data: Map<Int, Entry>, goal: Int): HabitState

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

        override fun currentState(data: Map<Int, Entry>, goal: Int): HabitState =
            HabitState(isStreakCurrently = isStreakCurrently(data, goal))

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
                            dataNextPosition =
                                if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }

                        dayNextPosition < dataNextPosition -> dayNextPosition.also {
                            dayNextValue = daysIterator.next()
                            dayNextPosition = findNextPosition(dayNextPosition, dayNextValue)
                        }

                        else -> dayNextPosition.also {
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
                            currentStreak[0] = 0
                        }

                        else -> {
                            val isPointedToGoal = dayNumber(daysAgo) == daysIterator.previousAndReturnBack(2)
                            if (isPointedToGoal) currentStreak[0]++
                            when {
                                isPointedToGoal && currentStreak[0] == days.size -> {
                                    val start = end(daysAgo)
                                    val end = start(daysAgo)
                                    val next = if (streaks.isEmpty()
                                        && daysIterator.nextAndReturnBack(1) != days.last()
                                        && isStreakCurrently(data, goal)
                                    ) findPosition(start, daysIterator.nextAndReturnBack(1)) + 1 else start
                                    counter.add(count = 1, start = end)
                                    counter.setStart(start = next)
                                    currentStreak[0] = 0
                                }

                                isPointedToGoal && dayNumber(daysAgo) == days.last() -> {
                                    val next = daysIterator.previousAndReturnBack(3)
                                    val position = findPosition(end(daysAgo), next) + 1
                                    counter.add(count = 1, start = position, end = start(daysAgo))
                                }

                                isPointedToGoal && dayNumber(daysAgo) == days.first() -> {
                                    val next = daysIterator.previousAndReturnBack()
                                    val position = findPosition(end(daysAgo), next) - 1
                                    counter.add(count = 1, start = end(daysAgo), end = position)
                                }

                                isPointedToGoal -> {
                                    val next = daysIterator.previousAndReturnBack()
                                    val prev = daysIterator.previousAndReturnBack(3)
                                    val positionNext = findPosition(end(daysAgo), next) - 1
                                    val positionPrev = findPosition(end(daysAgo), prev) + 1
                                    counter.add(count = 1, start = positionPrev, end = positionNext)
                                }

                                else -> {
                                    counter.add(count = 1, start = daysAgo)
                                }
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

        open fun transform(day: Int): Int = day

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

class LoopIterator<T>(private val data: List<T>) : ListIterator<T> {
    private var iterator: ListIterator<T> = data.listIterator()

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): T {
        if (!iterator.hasNext()) iterator = data.listIterator()
        return iterator.next()
    }

    override fun hasPrevious(): Boolean = iterator.hasPrevious()

    override fun nextIndex(): Int = iterator.nextIndex()

    override fun previous(): T {
        if (!iterator.hasPrevious()) iterator = data.listIterator(data.size)
        return iterator.previous()
    }

    override fun previousIndex(): Int = iterator.previousIndex()

    fun nextAndReturnBack(repeat: Int = 1): T {
        repeat(repeat - 1) { next() }
        val result = next()
        repeat(repeat) { previous() }
        return result
    }

    fun previousAndReturnBack(repeat: Int = 1): T {
        repeat(repeat - 1) { previous() }
        val result = previous()
        repeat(repeat) { next() }
        return result
    }
}