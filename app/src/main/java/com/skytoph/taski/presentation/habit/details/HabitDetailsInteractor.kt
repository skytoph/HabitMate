@file:OptIn(ExperimentalCoroutinesApi::class)

package com.skytoph.taski.presentation.habit.details

import androidx.paging.PagingData
import com.skytoph.taski.core.Now
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.datastore.settings.ChartFrequency
import com.skytoph.taski.domain.habit.EntryList
import com.skytoph.taski.domain.habit.Habit
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.details.mapper.BarChartEntriesUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.map
import com.skytoph.taski.presentation.habit.details.paging_source.BarChartEntriesPagerProvider
import com.skytoph.taski.presentation.habit.details.paging_source.EntityPagerProvider
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest

interface HabitDetailsInteractor : HabitDoneInteractor {
    fun entriesEditable(id: Long): Flow<PagingData<EditableHistoryUi>>
    fun entriesBarChart(id: Long): Flow<PagingData<BarItemUi>>
    fun entryList(id: Long): Flow<EntryList>
    fun entries(id: Long): Flow<HabitWithEntries?>
    fun habit(id: Long): Flow<Habit?>
    suspend fun habitDoneAndReturn(habit: HabitUi, daysAgo: Int): EntryUpdated
    suspend fun findMaxBarChartEntry(entries: EntryList): Int

    class Base(
        private val entriesPagerProvider: EntityPagerProvider,
        private val barChartPagerProvider: BarChartEntriesPagerProvider,
        private val entryMapper: EditableEntryDomainToUiMapper,
        private val barChartMapper: BarChartEntriesUiMapper,
        private val repository: HabitRepository,
        private val settings: SettingsCache,
        private val now: Now,
        interactor: HabitDoneInteractor,
    ) : HabitDetailsInteractor, HabitDoneInteractor by interactor {

        override fun entryList(id: Long): Flow<EntryList> = repository.entriesFlow(id)

        override fun entriesEditable(id: Long): Flow<PagingData<EditableHistoryUi>> =
            settings.state().distinctUntilChangedBy { it.habitHistoryView }.flatMapLatest { s ->
                entriesPagerProvider.getEntries(
                    id, s.streaksHighlighted, s.weekStartsOnSunday.value, s.habitHistoryView
                )
            }

        override fun entriesBarChart(id: Long): Flow<PagingData<BarItemUi>> =
            settings.state().distinctUntilChangedBy { it.habitDetailsView?.barChartFrequency }.flatMapLatest { s ->
                barChartPagerProvider.getEntries(
                    id,
                    s.habitDetailsView?.barChartFrequency ?: ChartFrequency.Default,
                    s.weekStartsOnSunday.value
                )
            }

        override suspend fun habitDoneAndReturn(habit: HabitUi, daysAgo: Int): EntryUpdated {
            val entry = habitDone(habit, daysAgo)
            val settings = settings.state().value
            val frequency = settings.habitDetailsView?.barChartFrequency.map()
            val isFirstDaySunday = settings.weekStartsOnSunday.value
            val index = frequency.index(daysAgo, now, isFirstDaySunday)
            val fromTimestamp = now.daysAgoInMillis(frequency.start(index, isFirstDaySunday))
            val toTimestamp = now.daysAgoInMillis(frequency.end(index, isFirstDaySunday))
            val history = repository.entries(habit.id, fromTimestamp, toTimestamp)
            val lastEntryTimestamp = repository.firstEntry(habit.id)?.timestamp
            val barChartEntry = barChartMapper.map(
                entries = history,
                index = frequency.index(daysAgo, now, isFirstDaySunday),
                frequency = frequency,
                isFirstDaySunday = isFirstDaySunday,
                lastEntryDaysAgo = lastEntryTimestamp?.let { now.daysAgo(it) }
            )
            return EntryUpdated(
                entry = entryMapper.map(daysAgo, entry.timesDone, habit.goal, null),
                barChartEntry = barChartEntry,
                barChartLastItemTimestamp = lastEntryTimestamp
            )
        }

        override suspend fun findMaxBarChartEntry(entries: EntryList): Int {
            val settings = settings.state().value
            return barChartMapper.findMaxTimesDone(
                entries = entries,
                frequency = settings.habitDetailsView?.barChartFrequency.map(),
                isFirstDaySunday = settings.weekStartsOnSunday.value
            ).let { if (it == 0) 1 else it }
        }

        override fun habit(id: Long) = repository.habitFlow(id)

        override fun entries(id: Long): Flow<HabitWithEntries?> =
            repository.habitWithEntriesFlow(id)
    }
}