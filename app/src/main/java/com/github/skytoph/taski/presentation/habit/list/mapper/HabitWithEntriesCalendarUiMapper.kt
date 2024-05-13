package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.presentation.habit.list.HabitsView
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class HabitWithEntriesCalendarUiMapper(
    habitMapper: HabitUiMapper, historyMapper: HabitHistoryUiMapper<HistoryUi, HabitsView.Calendar>
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, HabitsView.Calendar>(habitMapper, historyMapper)

class HabitWithEntriesDailyUiMapper(
    habitMapper: HabitUiMapper, historyMapper: HabitHistoryUiMapper<HistoryUi, HabitsView.Daily>
) : HabitWithHistoryUiMapper.Abstract<HistoryUi, HabitsView.Daily>(habitMapper, historyMapper)