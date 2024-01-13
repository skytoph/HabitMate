package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.presentation.core.ConvertIcon

interface HabitDomainMapper {
    fun map(id: Long, title: String, goal: Int, icon: ImageVector, color: Color): Habit

    class Base(private val convertIcon: ConvertIcon) : HabitDomainMapper {

        override fun map(id: Long, title: String, goal: Int, icon: ImageVector, color: Color) =
            Habit(
                id = id,
                title = title,
                goal = goal,
                iconName = convertIcon.getIconName(icon),
                color = color.toArgb(),
            )
    }
}