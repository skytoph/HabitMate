package com.github.skytoph.taski.presentation.core.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class HabitsProvider : PreviewParameterProvider<List<HabitWithHistoryUi<HistoryUi>>> {

    private val history =
        HistoryUi((0..363).map { EntryUi(percentDone = 1F / (it % 20)) }.toList())

    private val habits = IconsColors.allColors.mapIndexed { i, color ->
        HabitWithHistoryUi(HabitUi(i.toLong(), "habit", 1, color), history)
    }

    override val values: Sequence<List<HabitWithHistoryUi<HistoryUi>>> = sequenceOf(habits)
}

class HabitProvider : PreviewParameterProvider<HabitWithHistoryUi<HistoryUi>> {
    private val history =
        HistoryUi((1..364).map { EntryUi(percentDone = it % 4 * 0.3f) }.toList())

    private val habits = IconsColors.allColors.mapIndexed { i, color ->
        HabitWithHistoryUi(HabitUi(i.toLong(), "habit", 1, color), history)
    }

    override val values: Sequence<HabitWithHistoryUi<HistoryUi>> = habits.asSequence()

}

class HabitsEditableProvider : PreviewParameterProvider<List<EditableHistoryUi>> {
    private val history = (1..12).map { month ->
        EditableHistoryUi((0..28).associate { day ->
            day * month to EntryEditableUi(
                day = day.toString(),
                daysAgo = day * month,
                hasBorder = false
            )
        }, MonthUi(timestamp = month.toLong(), weeks = 4))
    }

    override val values: Sequence<List<EditableHistoryUi>> = sequenceOf(history)
}

class IconProvider : PreviewParameterProvider<IconsGroup> {
    override val values: Sequence<IconsGroup> = IconsGroup.allGroups.asSequence()
}