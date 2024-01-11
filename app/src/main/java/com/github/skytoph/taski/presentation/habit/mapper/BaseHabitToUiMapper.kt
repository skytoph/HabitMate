package com.github.skytoph.taski.presentation.habit.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.habit.HabitHistoryUiMapper
import com.github.skytoph.taski.domain.habit.HabitToUiMapper
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.core.ConvertIcon

class BaseHabitToUiMapper(
    private val convertIcon: ConvertIcon,
    private val now: Now,
    private val mapper: HabitHistoryUiMapper
) : HabitToUiMapper {

    override fun map(title: String, goal: Int, icon: String, color: Long, history: List<Long>) =
        HabitUi(
            title = title,
            goal = goal,
            icon = convertIcon.filledIconByName(icon),
            color = Color(color),
            history = mapper.map(history),
            todayPositions = HabitUi.MAX_DAYS - 8 + now.dayOfWeek()
        )
}