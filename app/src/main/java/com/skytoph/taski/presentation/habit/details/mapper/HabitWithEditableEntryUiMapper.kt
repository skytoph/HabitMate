package com.skytoph.taski.presentation.habit.details.mapper

import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper

class HabitWithEditableEntryUiMapper(
    habitMapper: HabitUiMapper,
    historyMapper: HabitHistoryUiMapper<EditableHistoryUi, ViewType>,
    statsMapper: HabitStatisticsMapper
) : HabitWithHistoryUiMapper.Abstract<EditableHistoryUi, ViewType>(
    habitMapper, historyMapper, statsMapper
)