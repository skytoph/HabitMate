package com.skytoph.taski.di.habit

import com.skytoph.taski.core.Now
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryCalendarUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryGridUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper
import com.skytoph.taski.presentation.habit.details.mapper.HabitWithEditableEntryUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.WeeksCache
import com.skytoph.taski.presentation.habit.edit.EditHabitValidator
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper
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