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
                    if (current.first != prev.first + 1 || !current.second.isCompleted(goal))
                        counter.save(streaks)
                    if (current.second.isCompleted(goal))
                        counter.add(count = 1, start = current.first)
                }
            }.apply { counter.save(this) }

    override fun isStreakCurrently(data: Map<Int, Entry>, goal: Int): Boolean {
        (0..1).forEach { index -> data[index]?.let { if (it.isCompleted(goal)) return true } }
        return false
    }

    override fun isScheduledForToday(data: Map<Int, Entry>, goal: Int): Boolean = true
}