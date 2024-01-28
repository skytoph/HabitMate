package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class HabitWithEntryUiMapper(
    convertIcon: ConvertIcon,
    colorMapper: ColorPercentMapper,
    historyMapper: HabitHistoryUiMapper<HistoryUi>
) : HabitToUiMapper.Abstract<HistoryUi>(convertIcon, colorMapper, historyMapper)