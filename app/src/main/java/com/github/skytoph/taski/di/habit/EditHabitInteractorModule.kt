package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.EntriesCache
import com.github.skytoph.taski.presentation.habit.edit.EditHabitInteractor
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.EditableEntryDomainToUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object EditHabitInteractorModule {

    @Provides
    fun interactor(
        now: Now,
        repository: HabitRepository,
        mapper: HabitDomainMapper,
        entryMapper: EditableEntryDomainToUiMapper,
        pagerProvider: EntityPagerProvider
    ): EditHabitInteractor =
        EditHabitInteractor.Base(mapper, pagerProvider, entryMapper, repository, now)

    @Provides
    fun pagerProvider(
        repository: HabitRepository, uiMapper: HabitHistoryUiMapper<EditableHistoryUi>,
    ): EntityPagerProvider = EntityPagerProvider(repository, uiMapper, EntriesCache())
}