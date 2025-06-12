package com.skytoph.taski.di.habit

import com.skytoph.taski.core.Now
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.skytoph.taski.presentation.habit.details.HabitDetailsInteractor
import com.skytoph.taski.presentation.habit.details.HabitDetailsState
import com.skytoph.taski.presentation.habit.details.mapper.BarChartEntriesUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryDomainToUiMapper
import com.skytoph.taski.presentation.habit.details.paging_source.BarChartEntriesPagerProvider
import com.skytoph.taski.presentation.habit.details.paging_source.EntityPagerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow

@Module
@InstallIn(ViewModelComponent::class)
object HabitDetailsViewModelModule {

    @Provides
    fun state(): MutableStateFlow<HabitDetailsState> = MutableStateFlow(HabitDetailsState())

    @Provides
    fun interactor(
        repository: HabitRepository,
        entryMapper: EditableEntryDomainToUiMapper,
        entriesPagerProvider: EntityPagerProvider,
        barChartPagerProvider: BarChartEntriesPagerProvider,
        barChartMapper: BarChartEntriesUiMapper,
        settings: SettingsCache,
        interactor: HabitDoneInteractor,
        now: Now
    ): HabitDetailsInteractor =
        HabitDetailsInteractor.Base(
            entriesPagerProvider,
            barChartPagerProvider,
            entryMapper,
            barChartMapper,
            repository,
            settings,
            now,
            interactor
        )
}