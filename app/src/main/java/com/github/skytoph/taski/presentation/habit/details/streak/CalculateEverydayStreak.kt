package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.Streak

class CalculateEverydayStreak(private val counter: StreakCounterCache = StreakCounterCache()) :
    CalculateStreak.Base() {

    override fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak> =
        if (data.isEmpty()) emptyList()
        else data.toList()
            .also { items ->
                val firstItem = items.first()
                if (firstItem.second.isCompleted(goal))
                    counter.add(count = 1, start = firstItem.first)
            }
            .zipWithNext()
            .fold(mutableListOf<Streak>()) { streaks, (prev, current) ->
                streaks.apply {
                    if (current.first != prev.first + 1)
                        counter.save(streaks)
                    if (current.second.isCompleted(goal))
                        counter.add(count = 1, start = current.first)
                }
            }.apply { counter.save(this) }

    override val skipMax: Int = 0

    override fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean =
        data.iterator().next().let { it.key == skipMax && it.value.isCompleted(goal) }
}