package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.mapper.Streak

class CalculateEverydayStreak(private val counter: StreakCounterCache = StreakCounterCache()) :
    CalculateStreak.Base() {

    override fun streaksList(data: Map<Int, Entry>, goal: Int): List<Streak> =
        if (data.isEmpty()) emptyList()
        else data.toList().zipWithNext().fold(mutableListOf<Streak>()) { streaks, (prev, current) ->
            streaks.apply {
                when {
                    prev.first == 0 && prev.second.isCompleted(goal) ->
                        counter.add(count = 1, start = 0)

                    current.first == prev.first + 1 && current.second.isCompleted(goal) ->
                        counter.add(count = 1, start = current.first)

                    else -> counter.save(streaks)
                }
            }
        }.apply { counter.save(this) }

    override fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean =
        data.iterator().next().let { it.key == 0 && it.value.isCompleted(goal) }
}