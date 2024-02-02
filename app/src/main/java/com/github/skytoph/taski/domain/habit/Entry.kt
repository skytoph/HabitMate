package com.github.skytoph.taski.domain.habit

data class Entry(
    val timestamp: Long,
    val timesDone: Int
)

data class EntryList(
    val entries: Map<Int, Entry>
)