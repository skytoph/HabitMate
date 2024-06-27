package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

interface CalculateInterval : StartAndEnd {

    class Week(
        now: Now,
        private val calculatePosition: StartAndEnd = StartAndEnd.Week(now)
    ) : CalculateInterval, Divide by Divide.Week(now, 1) {
        override fun start(index: Int): Int = calculatePosition.start(divide(index))
        override fun end(index: Int): Int = calculatePosition.end(divide(index))
    }

    class Month(
        now: Now,
        private val calculatePosition: StartAndEnd = StartAndEnd.Month(now)
    ) : CalculateInterval, Divide by Divide.Month(now, 1) {
        override fun start(index: Int): Int = calculatePosition.start(divide(index))
        override fun end(index: Int): Int = calculatePosition.end(divide(index))
    }
}