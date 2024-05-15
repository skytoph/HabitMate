package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class HabitWithEntriesCalendarUiMapper(
    habitMapper: HabitUiMapper, historyMapper: HabitHistoryUiMapper<HistoryUi, ViewType.Calendar>
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, ViewType.Calendar>(habitMapper, historyMapper)

class HabitWithEntriesDailyUiMapper(
    habitMapper: HabitUiMapper, historyMapper: HabitHistoryUiMapper<HistoryUi, ViewType.Daily>
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, ViewType.Daily>(habitMapper, historyMapper)