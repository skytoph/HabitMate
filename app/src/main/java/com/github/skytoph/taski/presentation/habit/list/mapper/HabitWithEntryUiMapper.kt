package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.list.EntryUi

class HabitWithEntryUiMapper(
    convertIcon: ConvertIcon,
    colorMapper: ColorPercentMapper,
    entryMapper: HabitEntryUiMapper<EntryUi>
) : HabitToUiMapper.Abstract<EntryUi>(convertIcon, colorMapper, entryMapper)