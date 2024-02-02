package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.EntriesUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithEntryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitsViewModelModule {

    @Provides
    fun habitWithEntryMapper(
        habitMapper: HabitUiMapper, mapper: HabitHistoryUiMapper<HistoryUi>,
    ): HabitWithHistoryUiMapper<HistoryUi> = HabitWithEntryUiMapper(habitMapper, mapper)

    @Provides
    fun historyMapper(now: Now): HabitHistoryUiMapper<HistoryUi> = EntriesUiMapper(now)

    @Provides
    fun state(): MutableState<HabitListState> = mutableStateOf(HabitListState())
}