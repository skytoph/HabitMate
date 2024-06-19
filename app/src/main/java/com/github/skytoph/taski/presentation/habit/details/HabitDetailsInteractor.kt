package com.github.skytoph.taski.presentation.habit.details

import androidx.compose.ui.graphics.Color
import androidx.paging.PagingData
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import kotlinx.coroutines.flow.Flow

interface HabitDetailsInteractor : HabitDoneInteractor {
    fun entries(id: Long): Flow<PagingData<EditableHistoryUi>>
    fun habit(id: Long): Flow<Habit?>
    fun mapData(data: EditableHistoryUi, entry: EntryEditableUi): EditableHistoryUi
    fun statistics(id: Long): Flow<HabitWithEntries>
    suspend fun entryEditable(
        id: Long, daysAgo: Int, goal: Int, habitColor: Color, defaultColor: Color
    ): EntryEditableUi

    class Base(
        private val pagerProvider: EntityPagerProvider,
        private val entryMapper: EditableEntryDomainToUiMapper,
        private val repository: HabitRepository,
        now: Now,
    ) : HabitDetailsInteractor,
        HabitDoneInteractor by HabitDoneInteractor.Base(repository, now) {

        override fun entries(id: Long): Flow<PagingData<EditableHistoryUi>> =
            pagerProvider.getEntries(id)

        override suspend fun entryEditable(
            id: Long, daysAgo: Int, goal: Int, habitColor: Color, defaultColor: Color
        ): EntryEditableUi = this.entry(id, daysAgo).let { entry ->
            entryMapper.map(daysAgo, entry.timesDone, goal)
        }

        override fun habit(id: Long) = repository.habitFlow(id)

        override fun statistics(id: Long): Flow<HabitWithEntries> =
            repository.habitWithEntriesFlow(id)

        override fun mapData(data: EditableHistoryUi, entry: EntryEditableUi): EditableHistoryUi {
            val index = data.entries.indexOfFirst { it.daysAgo == entry.daysAgo }
            return if (index == -1) data
            else data.copy(entries = data.entries.toMutableList().also { it[index] = entry })
        }
    }
}