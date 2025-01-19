package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.core.datastore.settings.ViewType
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.EntriesCalendarUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.EntriesDailyUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.EntryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitListUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithEntriesCalendarUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithEntriesDailyUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitsViewModelModule {

    @Provides
    fun habitWithEntriesDailyMapper(
        habitMapper: HabitUiMapper,
        mapper: HabitHistoryUiMapper<HistoryUi, ViewType.Daily>,
        statsMapper: HabitStatisticsMapper,
    ): HabitWithHistoryUiMapper<HistoryUi, ViewType.Daily> =
        HabitWithEntriesDailyUiMapper(habitMapper, mapper, statsMapper)

    @Provides
    fun habitWithEntriesCalendarMapper(
        habitMapper: HabitUiMapper,
        mapper: HabitHistoryUiMapper<HistoryUi, ViewType.Calendar>,
        statsMapper: HabitStatisticsMapper,
    ): HabitWithHistoryUiMapper<HistoryUi, ViewType.Calendar> =
        HabitWithEntriesCalendarUiMapper(habitMapper, mapper, statsMapper)

    @Provides
    fun entryMapper(): EntryUiMapper = EntryUiMapper.Base()

    @Provides
    fun historyCalendarMapper(now: Now, mapper: EntryUiMapper)
            : HabitHistoryUiMapper<HistoryUi, ViewType.Calendar> =
        EntriesCalendarUiMapper(now, mapper)

    @Provides
    fun historyDailyMapper(mapper: EntryUiMapper)
            : HabitHistoryUiMapper<HistoryUi, ViewType.Daily> = EntriesDailyUiMapper(mapper)

    @Provides
    fun habitsMapper(
        mapperDaily: HabitWithHistoryUiMapper<HistoryUi, ViewType.Daily>,
        mapperCalendar: HabitWithHistoryUiMapper<HistoryUi, ViewType.Calendar>,
    ): HabitListUiMapper = HabitListUiMapper.Base(mapperDaily, mapperCalendar)

    @Provides
    fun habitsViewMapper(mapper: HabitListUiMapper, stateMapper: StatisticsUiMapper): HabitsViewMapper =
        HabitsViewMapper.Base(mapper, stateMapper)

    @Provides
    fun state(): MutableState<HabitListState> = mutableStateOf(HabitListState())
}