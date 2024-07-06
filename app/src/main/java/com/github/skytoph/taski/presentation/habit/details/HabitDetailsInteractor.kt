package com.github.skytoph.taski.presentation.habit.details

import androidx.compose.ui.graphics.Color
import androidx.paging.PagingData
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
    fun statistics(id: Long): Flow<HabitWithEntries>
    suspend fun entryEditable(
        id: Long,
        daysAgo: Int,
        goal: Int,
        habitColor: Color,
        defaultColor: Color,
        statistics: HabitStatisticsUi
    ): EntryEditableUi

    class Base(
        private val pagerProvider: EntityPagerProvider,
        private val entryMapper: EditableEntryDomainToUiMapper,
        private val repository: HabitRepository,
        interactor: HabitDoneInteractor,
    ) : HabitDetailsInteractor, HabitDoneInteractor by interactor {

        override fun entries(id: Long): Flow<PagingData<EditableHistoryUi>> =
            pagerProvider.getEntries(id)

        override suspend fun entryEditable(
            id: Long,
            daysAgo: Int,
            goal: Int,
            habitColor: Color,
            defaultColor: Color,
            statistics: HabitStatisticsUi
        ): EntryEditableUi = this.entry(id, daysAgo).let { entry ->
            entryMapper.map(daysAgo, entry.timesDone, goal, statistics.isInRange(daysAgo))
        }

        override fun habit(id: Long) = repository.habitFlow(id)

        override fun statistics(id: Long): Flow<HabitWithEntries> =
            repository.habitWithEntriesFlow(id)
    }
}