package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.core.datastore.settings.ViewType
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryCalendarUiMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryGridUiMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitWithEditableEntryUiMapper
import com.github.skytoph.taski.presentation.habit.details.mapper.WeeksCache
import com.github.skytoph.taski.presentation.habit.edit.EditHabitValidator
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
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
        habitMapper: HabitUiMapper,
        mapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
        statsMapper: HabitStatisticsMapper,
    ): HabitWithHistoryUiMapper<EditableHistoryUi, ViewType> =
        HabitWithEditableEntryUiMapper(habitMapper, mapper, statsMapper)

    @Provides
    fun domainMapper(now: Now): HabitDomainMapper = HabitDomainMapper.Base(now)

    @Provides
    fun historyCalendarMapper(now: Now, mapper: EditableEntryDomainToUiMapper)
            : EditableEntryCalendarUiMapper = EditableEntryCalendarUiMapper(now, mapper)

    @Provides
    fun historyGridMapper(now: Now, weekCache: WeeksCache, mapper: EditableEntryDomainToUiMapper):
            EditableEntryGridUiMapper = EditableEntryGridUiMapper(now, weekCache, mapper)

    @Provides
    fun entryMapper(now: Now): EditableEntryDomainToUiMapper = EditableEntryDomainToUiMapper.Base(now)

    @Provides
    fun weekCache(): WeeksCache = WeeksCache()

    @Provides
    fun validator(): EditHabitValidator = EditHabitValidator()
}