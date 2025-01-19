package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.core.datastore.settings.ViewType
import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatisticsMapper
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class HabitWithEntriesCalendarUiMapper(
    habitMapper: HabitUiMapper,
    historyMapper: HabitHistoryUiMapper<HistoryUi, ViewType.Calendar>,
    statsMapper: HabitStatisticsMapper
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, ViewType.Calendar>(
    habitMapper,
    historyMapper,
    statsMapper
)

class HabitWithEntriesDailyUiMapper(
    habitMapper: HabitUiMapper,
    historyMapper: HabitHistoryUiMapper<HistoryUi, ViewType.Daily>,
    statsMapper: HabitStatisticsMapper
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, ViewType.Daily>(
    habitMapper,
    historyMapper,
    statsMapper
)