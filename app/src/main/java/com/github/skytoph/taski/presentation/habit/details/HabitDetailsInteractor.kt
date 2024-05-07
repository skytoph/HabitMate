package com.github.skytoph.taski.presentation.habit.details

import androidx.paging.PagingData
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.list.HabitDoneInteractor
import kotlinx.coroutines.flow.Flow

interface HabitDetailsInteractor : HabitDoneInteractor {
    fun entries(id: Long): Flow<PagingData<EditableHistoryUi>>
    fun habit(id: Long): Flow<Habit?>
    fun mapData(data: EditableHistoryUi, entry: EntryEditableUi): EditableHistoryUi
    suspend fun entryEditable(id: Long, daysAgo: Int): EntryEditableUi
    suspend fun delete(id: Long)

    class Base(
        private val pagerProvider: EntityPagerProvider,
        private val entryMapper: EditableEntryDomainToUiMapper,
        repository: HabitRepository,
        now: Now,
    ) : HabitDetailsInteractor, HabitDoneInteractor.Abstract(repository, now) {

        override fun entries(id: Long): Flow<PagingData<EditableHistoryUi>> =
            pagerProvider.getEntries(id)

        override suspend fun entryEditable(id: Long, daysAgo: Int): EntryEditableUi =
            entry(id, daysAgo).let { entry -> entryMapper.map(daysAgo, entry.timesDone) }

        override fun habit(id: Long) = repository.habitFlow(id)

        override suspend fun delete(id: Long) = repository.delete(id)

        override fun mapData(data: EditableHistoryUi, entry: EntryEditableUi): EditableHistoryUi {
            val index = data.entries.indexOfFirst { it.daysAgo == entry.daysAgo }
            return if (index == -1) data
            else data.copy(entries = data.entries.toMutableList().also { it[index] = entry })
        }
    }
}