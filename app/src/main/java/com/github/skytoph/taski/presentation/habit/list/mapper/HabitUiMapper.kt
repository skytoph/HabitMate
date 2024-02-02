package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitUiMapper {
    fun map(habit: Habit): HabitUi

    class Base(private val convertIcon: ConvertIcon) : HabitUiMapper {

        override fun map(habit: Habit): HabitUi = HabitUi(
            id = habit.id,
            title = habit.title,
            goal = habit.goal,
            icon = convertIcon.filledIconByName(habit.iconName),
            color = Color(habit.color),
        )
    }
}
