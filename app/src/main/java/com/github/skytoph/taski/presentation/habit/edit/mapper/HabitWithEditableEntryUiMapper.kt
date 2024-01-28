package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitToUiMapper

class HabitWithEditableEntryUiMapper(
    convertIcon: ConvertIcon,
    colorMapper: ColorPercentMapper,
    historyMapper: HabitHistoryUiMapper<EditableHistoryUi>
) : HabitToUiMapper.Abstract<EditableHistoryUi>(convertIcon, colorMapper, historyMapper)