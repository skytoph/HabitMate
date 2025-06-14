package com.skytoph.taski.domain.habit

data class Habit(
    val id: Long,
    val title: String,
    val description: String,
    val goal: Int,
    val iconName: String,
    val color: Int,
    val priority: Int = 0,
    val isArchived: Boolean = false,
    val frequency: Frequency = Frequency.Daily(),
    val reminder: Reminder = Reminder.None
)