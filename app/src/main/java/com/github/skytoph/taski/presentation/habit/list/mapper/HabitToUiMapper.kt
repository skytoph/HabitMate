package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitToUiMapper<T : HabitHistoryUi> {
    fun map(
        id: Long, title: String, goal: Int, icon: String, color: Int, history: Map<Int, Entry>
    ): HabitUi<T>

    abstract class Abstract<T : HabitHistoryUi>(
        private val convertIcon: ConvertIcon,
        private val colorMapper: ColorPercentMapper,
        private val historyMapper: HabitHistoryUiMapper<T>,
    ) : HabitToUiMapper<T> {

        override fun map(
            id: Long,
            title: String,
            goal: Int,
            icon: String,
            color: Int,
            history: Map<Int, Entry>
        ): HabitUi<T> = HabitUi(
            id = id,
            title = title,
            goal = goal,
            icon = convertIcon.filledIconByName(icon),
            color = Color(color),
            history = historyMapper.map(goal, history),
        )
    }
}