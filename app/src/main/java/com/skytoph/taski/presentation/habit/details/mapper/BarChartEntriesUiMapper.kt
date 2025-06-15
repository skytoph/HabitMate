package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.core.Now
import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.domain.habit.EntryList
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.details.BarItemUi
import com.skytoph.taski.presentation.habit.details.ChartFrequencyUi

interface BarChartEntriesUiMapper {
    fun map(
        entries: EntryList, index: Int, frequency: ChartFrequencyUi, isFirstDaySunday: Boolean, lastEntryDaysAgo: Int
    ): BarItemUi

    fun findMaxTimesDone(entries: EntryList, frequency: ChartFrequencyUi, isFirstDaySunday: Boolean): Int

    class Base(private val now: Now) : BarChartEntriesUiMapper {
        override fun map(
            entries: EntryList, index: Int, frequency: ChartFrequencyUi,
            isFirstDaySunday: Boolean, lastEntryDaysAgo: Int
        ): BarItemUi {
            val startDaysAgo = frequency.end(index, isFirstDaySunday)
            val endDaysAgo = frequency.start(index, isFirstDaySunday)
            val timesDone = getTimesDone(
                startDaysAgo = startDaysAgo,
                endDaysAgo = endDaysAgo,
                entries = entries
            )
            val dateLabel = StringResource.ResId(frequency.timePattern(startDaysAgo, now, isFirstDaySunday))
            return BarItemUi(
                timestamp = now.daysAgoInMillis(startDaysAgo),
                timesDone = timesDone,
                dateLabel = dateLabel,
                isEmpty = startDaysAgo > lastEntryDaysAgo
            )
        }

        private fun getTimesDone(startDaysAgo: Int, endDaysAgo: Int, entries: EntryList): Int =
            entries.entries.subMap(startDaysAgo, endDaysAgo + 1)
                .toList().sumOf { (_, entry) -> entry.timesDone }

        override fun findMaxTimesDone(entries: EntryList, frequency: ChartFrequencyUi, isFirstDaySunday: Boolean): Int {
            if (entries.entries.isEmpty()) return 0

            val iterator = entries.entries.iterator()
            val set = mutableSetOf<Int>()

            var entry: MutableMap.MutableEntry<Int, Entry>? = null

            while (iterator.hasNext()) {
                entry = iterator.next()
                val index = frequency.index(daysAgo = entry.key, now = now, isFirstDaySunday = isFirstDaySunday)
                val startDaysAgo = frequency.end(index, isFirstDaySunday)
                val endDaysAgo = frequency.start(index, isFirstDaySunday)
                set.addTimesDone(
                    startDaysAgo = startDaysAgo,
                    endDaysAgo = endDaysAgo,
                    entries = entries
                )
                while (entry!!.key < startDaysAgo && iterator.hasNext())
                    entry = iterator.next()
            }
            entry?.let {
                set.addTimesDone(
                    startDaysAgo = frequency.end(it.key, isFirstDaySunday),
                    endDaysAgo = frequency.start(entry.key, isFirstDaySunday),
                    entries = entries
                )
            }
            return set.max()
        }

        private fun MutableSet<Int>.addTimesDone(startDaysAgo: Int, endDaysAgo: Int, entries: EntryList) {
            add(
                getTimesDone(
                    startDaysAgo = startDaysAgo,
                    endDaysAgo = endDaysAgo,
                    entries = entries
                )
            )
        }
    }
}
