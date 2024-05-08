package com.github.skytoph.taski.presentation.habit.list.mapper


object ColorPercentMapper {
    fun toColorPercent(timesDone: Int, goal: Int): Float = when {
        timesDone <= 0 -> 0F
        timesDone >= goal -> 1F
        else -> percent(timesDone, goal)
    }

    fun percentDone(timesDone: Int, goal: Int): Int = percent(timesDone, goal).times(100).toInt()

    private fun percent(timesDone: Int, goal: Int) = timesDone.toFloat().div(goal)
}