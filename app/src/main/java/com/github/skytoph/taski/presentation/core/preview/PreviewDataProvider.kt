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

    private val history = HistoryUi((0..363).map { EntryUi(1F / (it % 20)) }.toList(), 362)

    private val habits = IconsColors.allColors.mapIndexed { i, color ->
        HabitWithHistoryUi(HabitUi(i.toLong(), "habit", 1, color), history)
    }

    override val values: Sequence<List<HabitWithHistoryUi<HistoryUi>>> = sequenceOf(habits)
}

class HabitProvider : PreviewParameterProvider<HabitWithHistoryUi<HistoryUi>> {
    private val history = HistoryUi((0..363).map { EntryUi(1F / (it % 20)) }.toList(), 362)

    private val habits = IconsColors.allColors.mapIndexed { i, color ->
        HabitWithHistoryUi(HabitUi(i.toLong(), "habit", 1, color), history)
    }

    override val values: Sequence<HabitWithHistoryUi<HistoryUi>> = habits.asSequence()

}

class HabitsEditableProvider : PreviewParameterProvider<List<EditableHistoryUi>> {

    private val entries =
        (0..28).map { EntryEditableUi((it).toString(), if (it == 4) 1 else 0, it) }

    private val months = (1..12).map { MonthUi(timestamp = it.toLong(), weeks = 4) }

    private val history = months.map { EditableHistoryUi(entries, it) }

    override val values: Sequence<List<EditableHistoryUi>> = sequenceOf(history)
}

class IconProvider : PreviewParameterProvider<IconsGroup> {
    override val values: Sequence<IconsGroup> = IconsGroup.allGroups.asSequence()
}