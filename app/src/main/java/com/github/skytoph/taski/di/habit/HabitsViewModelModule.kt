package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HabitsView
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitsViewModelModule {

    @Provides
    fun habitWithEntriesDailyMapper(
        habitMapper: HabitUiMapper, mapper: HabitHistoryUiMapper<HistoryUi, HabitsView.Daily>,
    ): HabitWithHistoryUiMapper<HistoryUi, HabitsView.Daily> =
        HabitWithEntriesDailyUiMapper(habitMapper, mapper)

    @Provides
    fun habitWithEntriesCalendarMapper(
        habitMapper: HabitUiMapper, mapper: HabitHistoryUiMapper<HistoryUi, HabitsView.Calendar>,
    ): HabitWithHistoryUiMapper<HistoryUi, HabitsView.Calendar> =
        HabitWithEntriesCalendarUiMapper(habitMapper, mapper)

    @Provides
    fun entryMapper(): EntryUiMapper = EntryUiMapper.Base()

    @Provides
    fun historyCalendarMapper(now: Now, mapper: EntryUiMapper)
            : HabitHistoryUiMapper<HistoryUi, HabitsView.Calendar> =
        EntriesCalendarUiMapper(now, mapper)

    @Provides
    fun historyDailyMapper(mapper: EntryUiMapper)
            : HabitHistoryUiMapper<HistoryUi, HabitsView.Daily> = EntriesDailyUiMapper(mapper)

    @Provides
    fun habitsMapper(
        mapperDaily: HabitWithHistoryUiMapper<HistoryUi, HabitsView.Daily>,
        mapperCalendar: HabitWithHistoryUiMapper<HistoryUi, HabitsView.Calendar>,
    ): HabitListUiMapper = HabitListUiMapper.Base(mapperDaily, mapperCalendar)

    @Provides
    fun state(): MutableState<HabitListState> = mutableStateOf(HabitListState())
}