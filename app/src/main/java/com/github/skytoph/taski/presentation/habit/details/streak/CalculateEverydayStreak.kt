package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.domain.habit.Entry

class CalculateEverydayStreak : CalculateStreak {

    override fun currentStreak(data: Map<Int, Entry>, goal: Int): Int =
        if (data.isEmpty()) 0
        else data.toList().zipWithNext().fold(1) { streak, (prev, current) ->
            if (current.first == prev.first + 1 && current.second.isCompleted(goal)) streak + 1
            else return streak
        }

    override fun streaks(data: Map<Int, Entry>, goal: Int): List<Int> =
        if (data.isEmpty()) listOf(0)
        else data.toList().zipWithNext().fold(mutableListOf(1)) { streaks, (prev, current) ->
            streaks.apply {
                if (current.first == prev.first + 1 && current.second.isCompleted(goal)) streaks[lastIndex] += 1
                else streaks.add(1)
            }
        }

    override fun maxStreak(data: Map<Int, Entry>, goal: Int): Int = streaks(data, goal).max()
}