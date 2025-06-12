package com.skytoph.taski.presentation.habit.details.paging_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import com.skytoph.taski.core.datastore.settings.HabitHistoryView
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.core.AbstractPagingSource
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryCalendarUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryGridUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import kotlinx.coroutines.flow.Flow

class EntryPagingSource(
    private val repository: HabitRepository,
    private val uiMapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
    private val statsMapper: StatisticsUiMapper,
    private val id: Long,
    private val isBorderOn: Boolean,
    private val isFirstDaySunday: Boolean,
) : AbstractPagingSource<EditableHistoryUi>() {

    override suspend fun getData(page: Int): EditableHistoryUi {
        val entries = repository.entries(id)
        val habit = repository.habit(id)
        val stats = statsMapper.statistics(HabitWithEntries(habit, entries), isFirstDaySunday)
        return uiMapper.map(
            page = page, goal = habit.goal, history = entries, stats = stats,
            isBorderOn = isBorderOn, isFirstDaySunday = isFirstDaySunday
        )
    }

    override fun EditableHistoryUi.isEmpty(): Boolean = entries.isEmpty()

    override fun getRefreshKey(state: PagingState<Int, EditableHistoryUi>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}

class EntityPagerProvider(
    private val repository: HabitRepository,
    private val gridUiMapper: EditableEntryGridUiMapper,
    private val calendarUiMapper: EditableEntryCalendarUiMapper,
    private val statsMapper: StatisticsUiMapper,
) {

    fun getEntries(id: Long, isBorderOn: Boolean, isFirstDaySunday: Boolean, historyView: HabitHistoryView)
            : Flow<PagingData<EditableHistoryUi>> =
        Pager(
            config = PagingConfig(
                pageSize = 1,
                prefetchDistance = 3,
                initialLoadSize = 6,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                val mapper =
                    if (historyView is HabitHistoryView.Calendar) calendarUiMapper else gridUiMapper.also { it.clear() }
                EntryPagingSource(repository, mapper, statsMapper, id, isBorderOn, isFirstDaySunday)
            }
        ).flow
}