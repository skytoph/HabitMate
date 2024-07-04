package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.core.alarm.AlarmScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.HabitCache
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.github.skytoph.taski.presentation.habit.edit.AddMonthMapper
import com.github.skytoph.taski.presentation.habit.edit.EditHabitInteractor
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.mapper.HabitNotificationMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
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
        alarm: AlarmScheduler,
        notificationMapper: HabitNotificationMapper,
        rescheduleMapper: AddMonthMapper,
    ): EditHabitInteractor = EditHabitInteractor.Base(
        mapper, repository, alarm, notificationMapper, rescheduleMapper, now
    )

    @Provides
    fun pagerProvider(
        repository: HabitRepository,
        uiMapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
        mapper: StatisticsUiMapper
    ): EntityPagerProvider = EntityPagerProvider(repository, uiMapper, mapper, HabitCache())
}