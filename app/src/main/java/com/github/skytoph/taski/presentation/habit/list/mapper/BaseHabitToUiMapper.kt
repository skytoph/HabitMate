package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.HabitToUiMapper
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.HabitUi

class BaseHabitToUiMapper(
    private val convertIcon: ConvertIcon,
    private val mapper: HabitHistoryUiMapper
) : HabitToUiMapper {

    override fun map(
        id: Long,
        title: String,
        goal: Int,
        icon: String,
        color: Int,
        history: List<Entry>
    ): HabitUi {
        val todayPosition = mapper.todayPosition()
        val habitHistory = mapper.map(history)
        val todayDonePercent = habitHistory[todayPosition]?.toFloat()?.div(goal) ?: 0F
        return HabitUi(
            id = id,
            title = title,
            goal = goal,
            icon = convertIcon.filledIconByName(icon),
            color = Color(color),
            history = habitHistory,
            todayPosition = todayPosition,
            todayDonePercent = todayDonePercent
        )
    }
}