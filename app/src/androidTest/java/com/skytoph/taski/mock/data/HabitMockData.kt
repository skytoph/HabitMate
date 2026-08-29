package com.skytoph.taski.mock.data

import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.ReminderUi
import com.skytoph.taski.presentation.habit.details.BarItemUi
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.skytoph.taski.presentation.habit.edit.MonthUi
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.skytoph.taski.presentation.habit.icon.IconsColors
import com.skytoph.taski.presentation.habit.list.EntryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi

object HabitMockData {
    val HABITS_WITH_HISTORY: List<HabitWithHistoryUi<HistoryUi>> = listOf(
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 1L,
                title = "Morning Exercise",
                description = "30 minutes of cardio or strength training",
                goal = 1,
                color = IconsColors.allColors[0],
                icon = IconResource.Id(R.drawable.dumbbell),
                priority = 1,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily())
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 1.0f, timesDone = 1, hasBorder = true),
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 0.5f, timesDone = 1, hasBorder = false),
                    3 to EntryUi(daysAgo = 3, percentDone = 1.0f, timesDone = 2, hasBorder = false),
                    5 to EntryUi(daysAgo = 5, percentDone = 0.8f, timesDone = 1, hasBorder = false),
                    7 to EntryUi(daysAgo = 7, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    9 to EntryUi(daysAgo = 9, percentDone = 0.6f, timesDone = 1, hasBorder = false)
                ),
                todayDonePercent = 1.0f,
                todayDone = 1,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 2L,
                title = "Read Books",
                description = "Read for at least 30 minutes daily",
                goal = 30,
                color = IconsColors.allColors[1],
                icon = IconResource.Id(R.drawable.book),
                priority = 2,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 0.7f, timesDone = 21, hasBorder = true),
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 30, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 0.9f, timesDone = 27, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 0.3f, timesDone = 9, hasBorder = false),
                    6 to EntryUi(daysAgo = 6, percentDone = 1.0f, timesDone = 35, hasBorder = false),
                    8 to EntryUi(daysAgo = 8, percentDone = 0.8f, timesDone = 24, hasBorder = false),
                    10 to EntryUi(daysAgo = 10, percentDone = 0.5f, timesDone = 15, hasBorder = false)
                ),
                todayDonePercent = 0.7f,
                todayDone = 21,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 3L,
                title = "Drink Water",
                description = "Stay hydrated throughout the day",
                goal = 8,
                color = IconsColors.allColors[2],
                icon = IconResource.Id(R.drawable.glass_water),
                priority = 3,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 0.9f, timesDone = 7, hasBorder = true),
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 8, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 0.8f, timesDone = 6, hasBorder = false),
                    3 to EntryUi(daysAgo = 3, percentDone = 1.0f, timesDone = 8, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 0.6f, timesDone = 5, hasBorder = false),
                    5 to EntryUi(daysAgo = 5, percentDone = 0.9f, timesDone = 7, hasBorder = false),
                    6 to EntryUi(daysAgo = 6, percentDone = 1.0f, timesDone = 8, hasBorder = false),
                    7 to EntryUi(daysAgo = 7, percentDone = 0.7f, timesDone = 6, hasBorder = false)
                ),
                todayDonePercent = 0.9f,
                todayDone = 7,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 4L,
                title = "Meditation",
                description = "Mindfulness practice for mental clarity",
                goal = 1,
                color = IconsColors.allColors[3],
                icon = IconResource.Id(R.drawable.self_improvement),
                priority = 4,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    3 to EntryUi(daysAgo = 3, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    6 to EntryUi(daysAgo = 6, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    8 to EntryUi(daysAgo = 8, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    10 to EntryUi(daysAgo = 10, percentDone = 1.0f, timesDone = 1, hasBorder = false)
                ),
                todayDonePercent = 0.0f,
                todayDone = 0,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 5L,
                title = "Learn Language",
                description = "Practice Spanish for 15 minutes",
                goal = 15,
                color = IconsColors.allColors[4],
                icon = IconResource.Id(R.drawable.notebook_pen),
                priority = 5,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 1.0f, timesDone = 20, hasBorder = true),
                    1 to EntryUi(daysAgo = 1, percentDone = 0.8f, timesDone = 12, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 1.0f, timesDone = 15, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 0.6f, timesDone = 9, hasBorder = false),
                    5 to EntryUi(daysAgo = 5, percentDone = 1.0f, timesDone = 18, hasBorder = false),
                    7 to EntryUi(daysAgo = 7, percentDone = 0.9f, timesDone = 13, hasBorder = false),
                    9 to EntryUi(daysAgo = 9, percentDone = 0.7f, timesDone = 10, hasBorder = false)
                ),
                todayDonePercent = 1.0f,
                todayDone = 20,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 6L,
                title = "Take Vitamins",
                description = "Daily vitamin supplement",
                goal = 1,
                color = IconsColors.allColors[5],
                icon = IconResource.Id(R.drawable.pill),
                priority = 6,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 1.0f, timesDone = 1, hasBorder = true),
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    3 to EntryUi(daysAgo = 3, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    6 to EntryUi(daysAgo = 6, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    7 to EntryUi(daysAgo = 7, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    8 to EntryUi(daysAgo = 8, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    9 to EntryUi(daysAgo = 9, percentDone = 1.0f, timesDone = 1, hasBorder = false)
                ),
                todayDonePercent = 1.0f,
                todayDone = 1,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 7L,
                title = "Journal Writing",
                description = "Reflect on the day in writing",
                goal = 1,
                color = IconsColors.allColors[6],
                icon = IconResource.Id(R.drawable.clipboard_pen_line),
                priority = 7,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    6 to EntryUi(daysAgo = 6, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    9 to EntryUi(daysAgo = 9, percentDone = 1.0f, timesDone = 1, hasBorder = false)
                ),
                todayDonePercent = 0.0f,
                todayDone = 0,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 8L,
                title = "Walk 10k Steps",
                description = "Daily step goal for health",
                goal = 10000,
                color = IconsColors.allColors[7],
                icon = IconResource.Id(R.drawable.nordic_walking),
                priority = 8,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 0.6f, timesDone = 6200, hasBorder = true),
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 12000, hasBorder = false),
                    2 to EntryUi(daysAgo = 2, percentDone = 0.8f, timesDone = 8500, hasBorder = false),
                    3 to EntryUi(daysAgo = 3, percentDone = 1.0f, timesDone = 11000, hasBorder = false),
                    5 to EntryUi(daysAgo = 5, percentDone = 0.7f, timesDone = 7300, hasBorder = false),
                    7 to EntryUi(daysAgo = 7, percentDone = 0.9f, timesDone = 9800, hasBorder = false),
                    8 to EntryUi(daysAgo = 8, percentDone = 1.0f, timesDone = 10500, hasBorder = false)
                ),
                todayDonePercent = 0.6f,
                todayDone = 6200,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 9L,
                title = "Practice Guitar",
                description = "Musical practice for skill development",
                goal = 45,
                color = IconsColors.allColors[8],
                icon = IconResource.Id(R.drawable.guitar),
                priority = 9,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    0 to EntryUi(daysAgo = 0, percentDone = 0.8f, timesDone = 36, hasBorder = true),
                    2 to EntryUi(daysAgo = 2, percentDone = 1.0f, timesDone = 50, hasBorder = false),
                    4 to EntryUi(daysAgo = 4, percentDone = 0.6f, timesDone = 25, hasBorder = false),
                    5 to EntryUi(daysAgo = 5, percentDone = 1.0f, timesDone = 60, hasBorder = false),
                    7 to EntryUi(daysAgo = 7, percentDone = 0.4f, timesDone = 20, hasBorder = false),
                    10 to EntryUi(daysAgo = 10, percentDone = 0.9f, timesDone = 40, hasBorder = false)
                ),
                todayDonePercent = 0.8f,
                todayDone = 36,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        ),
        HabitWithHistoryUi(
            habit = HabitUi(
                id = 10L,
                title = "Clean Room",
                description = "Maintain a tidy living space",
                goal = 1,
                color = IconsColors.allColors[9],
                icon = IconResource.Id(R.drawable.home),
                priority = 10,
                isArchived = false,
                frequency = FrequencyUi.Everyday(FrequencyUi.Daily()),
                reminder = ReminderUi()
            ),
            history = HistoryUi(
                entries = mapOf(
                    1 to EntryUi(daysAgo = 1, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    3 to EntryUi(daysAgo = 3, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    5 to EntryUi(daysAgo = 5, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    8 to EntryUi(daysAgo = 8, percentDone = 1.0f, timesDone = 1, hasBorder = false),
                    10 to EntryUi(daysAgo = 10, percentDone = 1.0f, timesDone = 1, hasBorder = false)
                ),
                todayDonePercent = 0.0f,
                todayDone = 0,
                fromDaysAgo = 0,
                toDaysAgo = 10
            )
        )
    )

    val HABIT: HabitUi = HABITS_WITH_HISTORY.first().habit

    val HISTORY: List<EditableHistoryUi> = (1..12).map { month ->
        EditableHistoryUi((0..27).associate { day ->
            (month - 1) * 28 + day to EntryEditableUi(
                day = (day + 1).toString(),
                daysAgo = (month - 1) * 28 + day,
                streakType = null,
                timesDone = day % 5
            )
        }, MonthUi(timestamp = month.toLong(), weeks = 4, index = month))
    }

    val BAR_CHART: List<BarItemUi> = (1..12).map { index ->
        BarItemUi(
            timestamp = index.toLong(),
            timesDone = index % 6,
            dateLabel = StringResource.ResId(R.string.pattern_month)
        )
    }
}