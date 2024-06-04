package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.data.habit.database.FrequencyEntity

data class Habit(
    val id: Long,
    val title: String,
    val goal: Int,
    val iconName: String,
    val color: Int,
    val priority: Int = 0,
    val isArchived: Boolean = false,
    val frequency: FrequencyEntity = FrequencyEntity.Daily()
)