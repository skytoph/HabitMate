package com.github.skytoph.taski.presentation.habit.list.view

import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.github.skytoph.taski.domain.habit.Habit

sealed interface SortHabits : ProvideOptionUi<SortHabits> {
    fun comparator(): Comparator<Pair<Int, Habit>>

    fun <T> sort(habits: List<T>, selector: (T) -> (Pair<Int, Habit>)): List<T> =
        habits.sortedWith(compareBy(comparator(), selector))

    fun sort(habits: List<Habit>): List<Habit> = sort(habits = habits, selector = { it.priority to it })

    override fun matches(item: SortHabits): Boolean = this == item

    data object ByTitle : SortHabits {
        override fun comparator(): Comparator<Pair<Int, Habit>> = compareBy { it.second.title }
        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionSortByTitle
    }

    data object ByColor : SortHabits {
        override fun comparator(): Comparator<Pair<Int, Habit>> = compareBy {
            val color = it.second.color
            val hsv = FloatArray(3)
            Color.RGBToHSV(color.red, color.green, color.blue, hsv)
            hsv[0].toInt() - hsv[1]
        }

        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionSortByColor
    }

    data object ByState : SortHabits {
        override fun comparator(): Comparator<Pair<Int, Habit>> = compareBy { it.first / it.second.goal }

        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionSortByState
    }

    data object Manually : SortHabits {
        override fun comparator(): Comparator<Pair<Int, Habit>> = compareBy { it.second.priority }
        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionSortManually
    }

    companion object {
        val options = listOf(ByTitle, ByColor, Manually, ByState)
    }
}