package com.skytoph.taski.di.habit

import com.skytoph.taski.core.Now
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.skytoph.taski.presentation.habit.details.mapper.BarChartEntriesUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryCalendarUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryGridUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.skytoph.taski.presentation.habit.details.paging_source.BarChartEntriesPagerProvider
import com.skytoph.taski.presentation.habit.details.paging_source.EntityPagerProvider
import com.skytoph.taski.presentation.habit.edit.EditHabitInteractor
import com.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object EditHabitInteractorModule {

    @Provides
    fun interactor(
        repository: HabitRepository,
        mapper: HabitDomainMapper,
        alarm: ReminderScheduler,
        habitInteractor: HabitDoneInteractor,
        notificationInteractor: NotificationInteractor
    ): EditHabitInteractor = EditHabitInteractor.Base(
        mapper, repository, alarm, notificationInteractor, habitInteractor
    )

    @Provides
    fun entriesPagerProvider(
        repository: HabitRepository,
        statsMapper: StatisticsUiMapper,
        gridMapper: EditableEntryGridUiMapper,
        calendarMapper: EditableEntryCalendarUiMapper,
        now: Now,
    ): EntityPagerProvider = EntityPagerProvider(
        repository = repository,
        gridUiMapper = gridMapper,
        calendarUiMapper = calendarMapper,
        statsMapper = statsMapper,
        now = now
    )

    @Provides
    fun barChartPagerProvider(
        repository: HabitRepository,
        mapper: BarChartEntriesUiMapper,
        now: Now,
    ): BarChartEntriesPagerProvider = BarChartEntriesPagerProvider(
        repository = repository,
        mapper = mapper,
        now = now
    )
}