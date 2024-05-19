package com.github.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsInteractor
import com.github.skytoph.taski.presentation.habit.details.HabitDetailsState
import com.github.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitDetailsViewModelModule {

    @Provides
    fun state(): MutableState<HabitDetailsState> = mutableStateOf(HabitDetailsState())

    @Provides
    fun interactor(
        now: Now,
        repository: HabitRepository,
        entryMapper: EditableEntryDomainToUiMapper,
        pagerProvider: EntityPagerProvider,
    ): HabitDetailsInteractor =
        HabitDetailsInteractor.Base(pagerProvider, entryMapper, repository, now)
}