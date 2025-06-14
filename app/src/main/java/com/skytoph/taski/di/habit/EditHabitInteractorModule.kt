package com.skytoph.taski.di.habit

import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryCalendarUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.EditableEntryGridUiMapper
import com.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.skytoph.taski.presentation.habit.edit.EditHabitInteractor
import com.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.skytoph.taski.presentation.habit.list.EntityPagerProvider
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
    fun pagerProvider(
        repository: HabitRepository,
        statsMapper: StatisticsUiMapper,
        gridMapper: EditableEntryGridUiMapper,
        calendarMapper: EditableEntryCalendarUiMapper,
    ): EntityPagerProvider = EntityPagerProvider(
        repository = repository,
        gridUiMapper = gridMapper,
        calendarUiMapper = calendarMapper,
        statsMapper = statsMapper
    )
}