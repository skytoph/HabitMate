package com.github.skytoph.taski.presentation.habit.list.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Segment
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.StringResource

object HabitsViewOptionsProvider {
    val optionCalendar = ViewOption(
        option = Option(
            title = StringResource.ResId(R.string.option_calendar),
            icon = Icons.Default.Segment,
        ), item = ViewType.Calendar()
    )
    val optionDaily = ViewOption(
        option = Option(
            title = StringResource.ResId(R.string.option_daily),
            icon = Icons.Default.Segment,
        ), item = ViewType.Daily()
    )
    val optionSortByTitle = SortOption(
        option = Option(
            title = StringResource.ResId(R.string.sort_by_title),
            icon = Icons.Default.Segment,
        ), item = SortHabits.ByTitle
    )
    val optionSortByColor = SortOption(
        option = Option(
            title = StringResource.ResId(R.string.sort_by_color),
            icon = Icons.Default.Segment,
        ), item = SortHabits.ByColor
    )
    val optionSortManually = SortOption(
        option = Option(
            title = StringResource.ResId(R.string.sort_manually),
            icon = Icons.Default.Segment,
        ), item = SortHabits.Manually
    )
    val optionFilterNone = FilterOption(
        option = Option(
            title = StringResource.ResId(R.string.filter_none),
            icon = Icons.Default.Segment,
        ), item = FilterHabits.None
    )
    val optionFilterByState = FilterOption(
        option = Option(
            title = StringResource.ResId(R.string.filter_by_state),
            icon = Icons.Default.Segment,
        ), item = FilterHabits.ByState
    )

    val viewOptions: List<ViewOption> = listOf(optionCalendar, optionDaily)
    val sortOptions: List<SortOption> =
        listOf(optionSortByTitle, optionSortByColor, optionSortManually)
    val filterOptions: List<FilterOption> = listOf(optionFilterNone, optionFilterByState)
}