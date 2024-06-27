package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.presentation.habit.details.mapper.HabitStatsUiMapper
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType

class HabitWithEntriesCalendarUiMapper(
    habitMapper: HabitUiMapper,
    historyMapper: HabitHistoryUiMapper<HistoryUi, ViewType.Calendar>,
    statsMapper: HabitStatsUiMapper
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, ViewType.Calendar>(
    habitMapper,
    historyMapper,
    statsMapper
)

class HabitWithEntriesDailyUiMapper(
    habitMapper: HabitUiMapper,
    historyMapper: HabitHistoryUiMapper<HistoryUi, ViewType.Daily>,
    statsMapper: HabitStatsUiMapper
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, ViewType.Daily>(
    habitMapper,
    historyMapper,
    statsMapper
)