package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.EditableEntryUiMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitWithEditableEntryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object EditHabitViewModelModule {

    @Provides
    fun habitWithEditableEntryMapper(
        habitMapper: HabitUiMapper, mapper: HabitHistoryUiMapper<EditableHistoryUi>,
    ): HabitWithHistoryUiMapper<EditableHistoryUi> =
        HabitWithEditableEntryUiMapper(habitMapper, mapper)

    @Provides
    fun domainMapper(convertIcon: ConvertIcon, now: Now): HabitDomainMapper =
        HabitDomainMapper.Base(convertIcon, now)

    @Provides
    fun entryMapper(now: Now): HabitHistoryUiMapper<EditableHistoryUi> = EditableEntryUiMapper(now)

    @Provides
    fun historyMapper(now: Now): EditableEntryUiMapper = EditableEntryUiMapper(now)

    @Provides
    fun uiMapper(convertIcon: ConvertIcon): HabitUiMapper = HabitUiMapper.Base(convertIcon)
}