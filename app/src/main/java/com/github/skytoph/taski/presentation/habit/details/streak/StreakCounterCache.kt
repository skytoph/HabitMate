package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.presentation.habit.details.Streak

class StreakCounterCache(
    private var start: Int = DEFAULT,
    private var end: Int = DEFAULT,
    private var counter: Int = 0,
) {
    init {
        clear()
    }

    fun setStart(start: Int) {
        if (start < this.start) this.start = start
    }

    fun add(count: Int, start: Int? = null, end: Int? = start) {
        if (count == 0) return
        if (start != null && this.start == DEFAULT) this.start = start
        if (end != null) this.end = end
        counter += count
    }

    fun save(list: MutableList<Streak>, addCount: Int = 0) {
        if (start == DEFAULT) return
        val item =
            if (start < end) Streak(start, end + addCount, counter)
            else Streak(end - addCount, start, counter)
        list.add(item)
        clear()
    }

    private fun clear() {
        start = DEFAULT
        end = start
        counter = 0
    }

    private companion object {
        const val DEFAULT = -1
    }
}