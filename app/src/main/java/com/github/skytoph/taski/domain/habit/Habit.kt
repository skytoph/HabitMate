package com.github.skytoph.taski.domain.habit

data class Habit(
    val id: Long,
    val title: String,
    val goal: Int,
    val iconName: String,
    val color: Int,
)