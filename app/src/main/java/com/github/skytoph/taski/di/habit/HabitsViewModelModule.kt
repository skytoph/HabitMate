package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.HabitToUiMapper
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.mapper.BaseHabitToUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitsViewModelModule {

    @Provides
    fun mapper(mapper: HabitHistoryUiMapper, convertIcon: ConvertIcon): HabitToUiMapper =
        BaseHabitToUiMapper(convertIcon, mapper)

    @Provides
    fun historyMapper(now: Now): HabitHistoryUiMapper = HabitHistoryUiMapper.Base(now)

    @Provides
    fun state(): MutableState<HabitListState> = mutableStateOf(HabitListState())
}