package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitEntryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitToUiMapper

class HabitWithEditableEntryUiMapper(
    convertIcon: ConvertIcon,
    colorMapper: ColorPercentMapper,
    entryMapper: HabitEntryUiMapper<EntryEditableUi>
) : HabitToUiMapper.Abstract<EntryEditableUi>(convertIcon, colorMapper, entryMapper)