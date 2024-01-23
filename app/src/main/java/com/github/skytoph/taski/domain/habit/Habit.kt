package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitToUiMapper

data class Habit(
    val id: Long,
    val title: String,
    val goal: Int,
    val iconName: String,
    val color: Int,
    val history: Map<Int, Entry> = emptyMap(),
) {
    fun <T : HabitEntryUi> map(mapper: HabitToUiMapper<T>) =
        mapper.map(id, title, goal, iconName, color, history)
}