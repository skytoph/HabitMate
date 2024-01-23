package com.github.skytoph.taski.presentation.habit.list.mapper

interface ColorPercentMapper {
    fun map(timesDone: Int, goal: Int): Float

    class Base : ColorPercentMapper {

        override fun map(timesDone: Int, goal: Int): Float =
            timesDone.toFloat().div(goal).let { if (it >= 1F) 1F else if (it <= 0F) 0F else it }
    }
}