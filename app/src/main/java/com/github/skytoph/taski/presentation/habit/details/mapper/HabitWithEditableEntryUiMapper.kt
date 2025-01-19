package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.datastore.settings.ViewType
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper

class HabitWithEditableEntryUiMapper(
    habitMapper: HabitUiMapper,
    historyMapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
    statsMapper: HabitStatisticsMapper
) : HabitWithHistoryUiMapper.Abstract<EditableHistoryUi, ViewType>(
    habitMapper, historyMapper, statsMapper
)