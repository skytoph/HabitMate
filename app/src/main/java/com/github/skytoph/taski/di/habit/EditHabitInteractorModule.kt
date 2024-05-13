package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.HabitCache
import com.github.skytoph.taski.presentation.habit.edit.EditHabitInteractor
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HabitsView
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
        now: Now, repository: HabitRepository, mapper: HabitDomainMapper,
    ): EditHabitInteractor =
        EditHabitInteractor.Base(mapper, repository, now)

    @Provides
    fun pagerProvider(
        repository: HabitRepository, uiMapper: HabitHistoryUiMapper<EditableHistoryUi, HabitsView>,
    ): EntityPagerProvider = EntityPagerProvider(repository, uiMapper, HabitCache())
}