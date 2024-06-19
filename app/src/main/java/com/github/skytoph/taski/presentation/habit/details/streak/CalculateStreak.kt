package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry

interface CalculateStreak {
    fun currentStreak(data: Map<Int, Entry>, goal: Int): Int
    fun maxStreak(data: Map<Int, Entry>, goal: Int): Int
    fun streaks(data: Map<Int, Entry>, goal: Int): List<Int>
    fun total(data: Map<Int, Entry>, goal: Int): Int =
        data.values.count { entry -> entry.timesDone >= goal }

    abstract class Abstract(private val days: Set<Int>) : CalculateStreak {

        private fun streaks(
            data: Map<Int, Entry>, goal: Int, days: List<Int>, findCurrentStreak: Boolean = false
        ): List<Int> =
            if (data.isEmpty() || days.isEmpty()) listOf(0)
            else {
                val streaks = mutableListOf(0)

                val dataIterator = data.keys.reversed().iterator()
                var daysIterator = days.listIterator()

                val today = dayNumber(0)
                var dayNextValue = daysIterator.next()
                    .also { if (!daysIterator.hasNext()) daysIterator = days.listIterator() }
                while (dayNextValue > today && daysIterator.hasNext()) {
                    dayNextValue = daysIterator.next()
                }
                if (!daysIterator.hasNext() && dayNextValue > today) {
                    daysIterator = days.listIterator()
                    dayNextValue = daysIterator.next()
                }

                var dayNextPosition = findNextPosition(-1, dayNextValue)
                var dataNextPosition = dataIterator.next()

                var daysAgo = 0
                while (dataNextPosition > BREAK_VALUE) {
                    daysAgo = when {
                        dayNextPosition > dataNextPosition -> dataNextPosition.also {
                            dataNextPosition =
                                if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }

                        dayNextPosition < dataNextPosition -> dayNextPosition.also {
                            if (!daysIterator.hasNext()) daysIterator = days.listIterator()
                            dayNextPosition = findNextPosition(dayNextPosition, daysIterator.next())
                        }

                        else -> dayNextPosition.also {
                            if (!daysIterator.hasNext()) daysIterator = days.listIterator()
                            dayNextPosition = findNextPosition(dayNextPosition, daysIterator.next())
                            dataNextPosition =
                                if (dataIterator.hasNext()) dataIterator.next() else BREAK_VALUE
                        }
                    }

                    val entry = data[daysAgo]

                    when {
                        entry == null || entry.timesDone < goal ->
                            if (findCurrentStreak) break
                            else if (streaks[streaks.lastIndex] > 0) streaks.add(0)

                        entry.timesDone >= goal ->
                            streaks[streaks.lastIndex] += 1
                    }
                }
                streaks
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

        override fun currentStreak(data: Map<Int, Entry>, goal: Int): Int =
            streaks(data = data, goal = goal, days = days.reversed(), findCurrentStreak = true)
                .first()

        override fun streaks(data: Map<Int, Entry>, goal: Int): List<Int> =
            streaks(data = data, goal = goal, days = days.reversed(), findCurrentStreak = false)

        override fun maxStreak(data: Map<Int, Entry>, goal: Int): Int =
            streaks(data = data, goal = goal, days = days.reversed(), findCurrentStreak = false)
                .max()

        abstract fun dayNumber(daysAgo: Int): Int

        companion object {
            private const val BREAK_VALUE = -1
        }
    }
}
