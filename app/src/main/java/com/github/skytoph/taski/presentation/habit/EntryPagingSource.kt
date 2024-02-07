package com.github.skytoph.taski.presentation.habit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import kotlinx.coroutines.flow.Flow

class EntryPagingSource(
    private val repository: HabitRepository,
    private val uiMapper: HabitHistoryUiMapper<EditableHistoryUi>,
    private val entryCache: EntriesCache,
    private val id: Long
) : PagingSource<Int, EditableHistoryUi>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EditableHistoryUi> {
        val page = params.key ?: 0

        return try {
            entryCache.cacheIfEmpty(repository.entries(id))
            val history = uiMapper.map(history = entryCache.get(), page = page)

            LoadResult.Page(
                data = listOf(history),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (history.entries.isEmpty()) null else page + 1
            )
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

class EntriesCache(private val data: MutableList<EntryList> = ArrayList()) {
    fun cacheIfEmpty(entries: EntryList) {
        if (data.isEmpty()) data.add(entries)
    }

    fun get(): EntryList = data[0]
}

class EntityPagerProvider(
    private val repository: HabitRepository,
    private val uiMapper: HabitHistoryUiMapper<EditableHistoryUi>,
    private val entryCache: EntriesCache
) {
    fun getEntries(id: Long): Flow<PagingData<EditableHistoryUi>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            prefetchDistance = 90,
            initialLoadSize = 90,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { EntryPagingSource(repository, uiMapper, entryCache, id) }
    ).flow
}