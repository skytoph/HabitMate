package com.github.skytoph.taski.presentation.habit.list.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Segment
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

object HabitsViewOptionsProvider {
    val optionCalendar = ViewOption(
        option = Option(
            title = StringResource.ResId(R.string.option_calendar),
            icon = IconResource.Id(R.drawable.rows_2),
        ), item = ViewType.Calendar()
    )
    val optionDaily = ViewOption(
        option = Option(
            title = StringResource.ResId(R.string.option_daily),
            icon = IconResource.Id(R.drawable.rows_3),
        ), item = ViewType.Daily()
    )
    val optionSortByTitle = SortOption(
        option = Option(
            title = StringResource.ResId(R.string.sort_by_title),
            icon = IconResource.Id(R.drawable.sort_characters),
        ), item = SortHabits.ByTitle
    )
    val optionSortByColor = SortOption(
        option = Option(
            title = StringResource.ResId(R.string.sort_by_color),
            icon = IconResource.Id(R.drawable.sort_color),
        ), item = SortHabits.ByColor
    )
    val optionSortManually = SortOption(
        option = Option(
            title = StringResource.ResId(R.string.sort_manually),
            icon = IconResource.Id(R.drawable.sort_numbers),
        ), item = SortHabits.Manually
    )
    val optionFilterNone = FilterOption(
        option = Option(
            title = StringResource.ResId(R.string.filter_none),
            icon = IconResource.Id(R.drawable.filter_x),
        ), item = FilterHabits.None
    )
    val optionFilterByState = FilterOption(
        option = Option(
            title = StringResource.ResId(R.string.filter_by_state),
            icon = IconResource.Id(R.drawable.filter_check),
        ), item = FilterHabits.ByState
    )

    val viewOptions: List<ViewOption> = listOf(optionCalendar, optionDaily)
    val sortOptions: List<SortOption> =
        listOf(optionSortByTitle, optionSortByColor, optionSortManually)
    val filterOptions: List<FilterOption> = listOf(optionFilterNone, optionFilterByState)
}