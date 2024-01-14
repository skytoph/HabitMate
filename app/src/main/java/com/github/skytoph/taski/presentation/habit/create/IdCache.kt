package com.github.skytoph.taski.presentation.habit.create

import com.github.skytoph.taski.core.MutableCache
import com.github.skytoph.taski.presentation.habit.HabitUi

class IdCache(private var cachedId: Long = HabitUi.ID_DEFAULT) : MutableCache<Long> {

    override fun cache(value: Long) {
        cachedId = value
    }

    override fun get(): Long = cachedId
}