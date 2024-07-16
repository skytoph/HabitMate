package com.github.skytoph.taski.presentation.habit.list.mapper

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitDomainMapper {
    fun map(habit: HabitUi, context: Context): Habit

    class Base(private val now: Now) : HabitDomainMapper {

        override fun map(habit: HabitUi, context: Context) = Habit(
            id = if (habit.id == HabitUi.ID_DEFAULT) now.milliseconds() else habit.id,
            title = habit.title,
            description = habit.description,
            goal = habit.goal,
            iconName = habit.icon.name(context.resources),
            color = habit.color.toArgb(),
            priority = habit.priority,
            isArchived = habit.isArchived,
            frequency = habit.frequency.map(),
            reminder = habit.reminder.map()
        )
    }
}