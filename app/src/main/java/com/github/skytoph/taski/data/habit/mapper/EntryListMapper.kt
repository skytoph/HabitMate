package com.github.skytoph.taski.data.habit.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.domain.habit.EntryList

interface EntryListMapper {
    fun map(entries: List<EntryEntity>): EntryList

    class Base(private val now: Now) : EntryListMapper {

        override fun map(entries: List<EntryEntity>): EntryList =
            EntryList(entries.associateBy({ now.daysAgo(it.timestamp) }, { it.toEntry() }))
    }
}
