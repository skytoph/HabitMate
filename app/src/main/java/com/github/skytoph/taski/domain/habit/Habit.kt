package com.github.skytoph.taski.domain.habit

data class Habit(
    val id: Long,
    val title: String,
    val goal: Int,
    val iconName: String,
    val color: Int,
    val history: List<Long> = emptyList(),
) {
    fun map(mapper: HabitToUiMapper) = mapper.map(id, title, goal, iconName, color, history)
}