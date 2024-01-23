package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.IdCache
import com.github.skytoph.taski.presentation.habit.edit.mapper.EditableEntryUiMapper
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitWithEditableEntryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitEntryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitToUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object EditHabitViewModelModule {

    @Provides
    fun habitWithEditableEntryMapper(
        mapper: HabitEntryUiMapper<EntryEditableUi>,
        convertIcon: ConvertIcon,
        colorMapper: ColorPercentMapper
    ): HabitToUiMapper<EntryEditableUi> =
        HabitWithEditableEntryUiMapper(convertIcon, colorMapper, mapper)

    @Provides
    fun domainMapper(convertIcon: ConvertIcon, now: Now): HabitDomainMapper =
        HabitDomainMapper.Base(convertIcon, now)

    @Provides
    fun entryMapper(now: Now, colorMapper: ColorPercentMapper): HabitEntryUiMapper<EntryEditableUi> =
        EditableEntryUiMapper(now, colorMapper)

    @Provides
    fun historyMapper(now: Now, colorMapper: ColorPercentMapper): EditableEntryUiMapper =
        EditableEntryUiMapper(now, colorMapper)

    @Provides
    fun colorMapper(): ColorPercentMapper = ColorPercentMapper.Base()
}