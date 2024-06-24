package com.github.skytoph.taski.domain.habit

data class Entry(
    val timestamp: Long,
    val timesDone: Int
) {
    fun isCompleted(goal: Int): Boolean = timesDone >= goal
}

data class EntryList(
    val entries: Map<Int, Entry>
)