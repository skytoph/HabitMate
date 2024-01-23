package com.github.skytoph.taski.presentation.habit.edit

import com.github.skytoph.taski.core.MutableCache
import com.github.skytoph.taski.presentation.habit.HabitUi
//todo remove
class IdCache(private var cachedId: Long = HabitUi.ID_DEFAULT) : MutableCache<Long> {

    override fun cache(value: Long) {
        cachedId = value
    }

    override fun get(): Long = cachedId
}