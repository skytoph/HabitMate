package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi

interface HabitDomainMapper {
    fun map(
        id: Long,
        title: String,
        goal: Int,
        color: Color,
        iconName: String,
        priority: Int,
        isArchived: Boolean,
        frequency: FrequencyUi
    ): Habit

    class Base(private val now: Now) : HabitDomainMapper {

        override fun map(
            id: Long,
            title: String,
            goal: Int,
            color: Color,
            iconName: String,
            priority: Int,
            isArchived: Boolean,
            frequency: FrequencyUi
        ) = Habit(
            id = if (id == HabitUi.ID_DEFAULT) now.milliseconds() else id,
            title = title,
            goal = goal,
            iconName = iconName,
            color = color.toArgb(),
            priority = priority,
            isArchived = isArchived,
            frequency = frequency.map()
        )
    }
}