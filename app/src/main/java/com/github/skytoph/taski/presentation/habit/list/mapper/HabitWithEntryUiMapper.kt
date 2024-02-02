package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class HabitWithEntryUiMapper(
    habitMapper: HabitUiMapper, historyMapper: HabitHistoryUiMapper<HistoryUi>
) : HabitWithHistoryUiMapper.Abstract<HistoryUi>(habitMapper, historyMapper)