package com.skytoph.taski.presentation.habit.details.paging_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skytoph.taski.core.Now
import com.skytoph.taski.core.datastore.settings.ChartFrequency
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.AbstractPagingSource
import com.skytoph.taski.presentation.habit.details.BarItemUi
import com.skytoph.taski.presentation.habit.details.mapper.BarChartEntriesUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.map
import kotlinx.coroutines.flow.Flow

class BarChartEntriesSource(
    private val repository: HabitRepository,
    private val mapper: BarChartEntriesUiMapper,
    private val id: Long,
    private val isFirstDaySunday: Boolean,
    private val frequency: ChartFrequency,
    private val now: Now,
) : AbstractPagingSource<BarItemUi>() {

    override suspend fun getData(page: Int): BarItemUi {
        val frequencyUi = frequency.map()
        val fromTimestamp = now.daysAgoInMillis(frequencyUi.start(page, isFirstDaySunday))
        val toTimestamp = now.daysAgoInMillis(frequencyUi.end(page, isFirstDaySunday))
        val entries = repository.entries(id, fromTimestamp, toTimestamp)
        val lastEntryDaysAgo = repository.firstEntry(id)?.timestamp?.let { now.daysAgo(it) }
        val history = mapper.map(
            entries = entries,
            index = page,
            frequency = frequencyUi,
            isFirstDaySunday = isFirstDaySunday,
            lastEntryDaysAgo = lastEntryDaysAgo
        )
        return history
    }

    override fun BarItemUi.isEmpty(): Boolean = this.timestamp <= 0L
}

class BarChartEntriesPagerProvider(
    private val repository: HabitRepository,
    private val mapper: BarChartEntriesUiMapper,
    private val now: Now,
) {

    fun getEntries(id: Long, frequency: ChartFrequency, isFirstDaySunday: Boolean): Flow<PagingData<BarItemUi>> =
        Pager(
            config = PagingConfig(
                pageSize = 1,
                prefetchDistance = 3,
                initialLoadSize = 6,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BarChartEntriesSource(repository, mapper, id, isFirstDaySunday, frequency, now)
            }
        ).flow
}