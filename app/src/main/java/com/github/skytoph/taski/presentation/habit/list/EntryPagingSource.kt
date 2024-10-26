package com.github.skytoph.taski.presentation.habit.list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EntryPagingSource(
    private val repository: HabitRepository,
    private val uiMapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
    private val statsMapper: StatisticsUiMapper,
    private val id: Long,
    private val isBorderOn: Boolean,
    private val isFirstDaySunday: Boolean,
) : PagingSource<Int, EditableHistoryUi>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EditableHistoryUi> {
        val page = params.key ?: 0

        return try {
            withContext(Dispatchers.IO) {
                val entries = repository.entries(id)
                val habit = repository.habit(id)
                val stats = statsMapper.map(HabitWithEntries(habit, entries), isFirstDaySunday)
                val history = uiMapper.map(
                    page = page, goal = habit.goal, history = entries, stats = stats,
                    isBorderOn = isBorderOn, isFirstDaySunday = isFirstDaySunday
                )

                LoadResult.Page(
                    data = listOf(history),
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (history.entries.isEmpty()) null else page + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EditableHistoryUi>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}

class HabitCache(private val data: MutableList<EntryList> = ArrayList()) {
    suspend fun cacheIfEmpty(fetch: suspend () -> EntryList) {
        if (data.isEmpty()) data.add(fetch())
    }

    fun get(): EntryList = data[0]
}

class EntityPagerProvider(
    private val repository: HabitRepository,
    private val uiMapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
    private val statsMapper: StatisticsUiMapper,
    private val entryCache: HabitCache
) {
    private var dataSource: EntryPagingSource? = null

    fun getEntries(id: Long, isBorderOn: Boolean, isFirstDaySunday: Boolean): Flow<PagingData<EditableHistoryUi>> = Pager(
        config = PagingConfig(
            pageSize = 1,
            prefetchDistance = 3,
            initialLoadSize = 6,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            EntryPagingSource(repository, uiMapper, statsMapper, id, isBorderOn, isFirstDaySunday)
                .also { dataSource = it }
        }
    ).flow
}