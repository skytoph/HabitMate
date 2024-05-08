package com.github.skytoph.taski.presentation.habit.details.mapper

class WeeksCache(private var weeks: Int = 0) {

    fun add(weeks: Int) {
        this.weeks += weeks
    }

    fun get(): Int = weeks
}