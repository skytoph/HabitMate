package com.skytoph.taski.data.habit.mapper

import com.skytoph.taski.core.Now
import com.skytoph.taski.data.habit.database.EntryEntity
import com.skytoph.taski.domain.habit.EntryList

interface EntryListMapper {
    fun map(entries: List<EntryEntity>): EntryList

    class Base(private val now: Now) : EntryListMapper {

        override fun map(entries: List<EntryEntity>): EntryList =
            EntryList(entries.associateBy({ now.daysAgo(it.timestamp) }, { it.toEntry() }))
    }
}
