package com.github.skytoph.taski.presentation.core.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup
import com.github.skytoph.taski.presentation.habit.icon.IconsLockedGroup
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.settings.restore.BackupItemUi

class HabitsProvider : PreviewParameterProvider<List<HabitWithHistoryUi<HistoryUi>>> {

    private val history =
        HistoryUi((0..363).map { EntryUi(percentDone = 1F / (it % 20)) }.toList())

    private val habits = IconsColors.allColors.mapIndexed { i, color ->
        HabitWithHistoryUi(HabitUi(i.toLong(), "habit", "", 1, color), history)
    }

    override val values: Sequence<List<HabitWithHistoryUi<HistoryUi>>> = sequenceOf(habits)
}

class HabitProvider : PreviewParameterProvider<HabitWithHistoryUi<HistoryUi>> {
    private val history =
        HistoryUi((1..364).map { EntryUi(percentDone = it % 4 * 0.3f) }.toList())

    private val habits = IconsColors.allColors.mapIndexed { i, color ->
        HabitWithHistoryUi(HabitUi(i.toLong(), "habit", "", 1, color), history)
    }

    override val values: Sequence<HabitWithHistoryUi<HistoryUi>> = habits.asSequence()

}

class HabitsEditableProvider : PreviewParameterProvider<List<EditableHistoryUi>> {
    private val history = (1..12).map { month ->
        EditableHistoryUi((0..28).associate { day ->
            day * month to EntryEditableUi(
                day = day.toString(),
                daysAgo = day * month,
                hasBorder = day % 7 == 0
            )
        }, MonthUi(timestamp = month.toLong(), weeks = 4, index = 1))
    }

    override val values: Sequence<List<EditableHistoryUi>> = sequenceOf(history)
}

class IconProvider : PreviewParameterProvider<IconsGroup<Int>> {
    override val values: Sequence<IconsGroup<Int>> = IconsGroup.allGroups.asSequence()
}

class IconLockedProvider : PreviewParameterProvider<List<IconsLockedGroup>> {
    override val values: Sequence<List<IconsLockedGroup>> = sequenceOf(IconsGroup.allGroups.map { group ->
        IconsLockedGroup(
            titleResId = group.title,
            icons = group.icons.mapIndexed { index, icon -> icon to (index % 5 == 0) })
    })
}

class BackupItemsProvider : PreviewParameterProvider<List<BackupItemUi>> {
    override val values: Sequence<List<BackupItemUi>> =
        sequenceOf(MutableList(15) { BackupItemUi(date = "$it apr. 2024 19:05:11", size = "$it b") })
}