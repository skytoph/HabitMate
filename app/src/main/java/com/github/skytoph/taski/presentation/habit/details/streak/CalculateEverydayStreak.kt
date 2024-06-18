package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry

class CalculateEverydayStreak : CalculateStreak {
    override fun currentStreak(data: Map<Int, Entry>, goal: Int, days: Set<Int>): Int =
        if (data.isEmpty()) 0
        else data.toList().zipWithNext().fold(1) { streak, (prev, current) ->
            if (current.first == prev.first + 1 && current.second.timesDone >= goal) streak + 1
            else return streak
        }

    override fun streaks(data: Map<Int, Entry>, goal: Int, days: Set<Int>): List<Int> =
        if (data.isEmpty()) emptyList()
        else data.toList().zipWithNext().fold(mutableListOf(1)) { streaks, (prev, current) ->
            streaks.apply {
                if (current.first == prev.first + 1 && current.second.timesDone >= goal) streaks[lastIndex] += 1
                else streaks.add(1)
            }
        }
}