package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

interface TransformWeekDay {
    fun transform(day: Int): Int

    class Base(private val now: Now) : TransformWeekDay {
        override fun transform(day: Int): Int = if (now.default) day else (day + 5) % 7 + 1
    }
}
