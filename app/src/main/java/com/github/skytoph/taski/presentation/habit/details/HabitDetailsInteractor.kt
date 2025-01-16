@file:OptIn(ExperimentalCoroutinesApi::class)

package com.github.skytoph.taski.presentation.habit.details

import androidx.paging.PagingData
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.list.EntityPagerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

interface HabitDetailsInteractor : HabitDoneInteractor {
    fun entries(id: Long): Flow<PagingData<EditableHistoryUi>>
    fun habit(id: Long): Flow<Habit?>
    fun statistics(id: Long): Flow<HabitWithEntries?>
    suspend fun habitDoneAndReturn(habit: HabitUi, daysAgo: Int): EntryEditableUi

    class Base(
        private val pagerProvider: EntityPagerProvider,
        private val entryMapper: EditableEntryDomainToUiMapper,
        private val repository: HabitRepository,
        private val settings: SettingsCache,
        interactor: HabitDoneInteractor,
    ) : HabitDetailsInteractor, HabitDoneInteractor by interactor {

        override fun entries(id: Long): Flow<PagingData<EditableHistoryUi>> =
            settings.state().flatMapLatest { s ->
                pagerProvider.getEntries(id, s.streaksHighlighted, s.weekStartsOnSunday.value, s.isHabitHistoryCalendar)
            }

        override suspend fun habitDoneAndReturn(habit: HabitUi, daysAgo: Int): EntryEditableUi {
            val entry = habitDone(habit, daysAgo)
            return entryMapper.map(daysAgo, entry.timesDone, habit.goal, null)
        }

        override fun habit(id: Long) = repository.habitFlow(id)

        override fun statistics(id: Long): Flow<HabitWithEntries?> =
            repository.habitWithEntriesFlow(id)
    }
}