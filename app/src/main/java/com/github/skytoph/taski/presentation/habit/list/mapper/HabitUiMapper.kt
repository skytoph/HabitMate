package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi

interface HabitUiMapper {
    fun map(habit: Habit): HabitUi

    class Base : HabitUiMapper {

        override fun map(habit: Habit): HabitUi = HabitUi(
            id = habit.id,
            title = habit.title,
            goal = habit.goal,
            icon = IconResource.Name(habit.iconName),
            color = Color(habit.color),
            priority = habit.priority,
            isArchived = habit.isArchived,
            frequency = habit.frequency.let { if (it.isEveryday()) FrequencyUi.Everyday(it.mapToUi()) else it.mapToUi() },
            reminder = habit.reminder.mapToUi()
        )
    }
}
