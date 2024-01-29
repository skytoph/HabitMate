package com.github.skytoph.taski.presentation.habit.list.mapper


object ColorPercentMapper{
    fun toColorPercent(timesDone: Int, goal: Int): Float =
        timesDone.toFloat().div(goal).let { if (it >= 1F) 1F else if (it <= 0F) 0F else it }

    fun percentDone(timesDone: Int, goal: Int): Int =
        timesDone.toFloat().div(goal).times(100).toInt()
}