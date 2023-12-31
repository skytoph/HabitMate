package com.github.skytoph.taski.domain.habit

data class Habit(
    val title: String,
    val iconName: String,
    val color: Long,
    val history: List<Long>,
) {
    fun map(mapper: HabitToUiMapper) = mapper.map(title, iconName, color, history)
}