package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

object HabitsViewOptionsProvider {
    val optionCalendar = ViewOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.option_calendar),
            icon = IconResource.Id(R.drawable.rows_2),
        ), data = ViewType.Calendar()
    )
    val optionDaily = ViewOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.option_daily),
            icon = IconResource.Id(R.drawable.rows_3),
        ), data = ViewType.Daily()
    )
    val optionSortByTitle = SortOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.sort_by_title),
            icon = IconResource.Id(R.drawable.sort_characters),
        ), data = SortHabits.ByTitle
    )
    val optionSortByColor = SortOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.sort_by_color),
            icon = IconResource.Id(R.drawable.sort_color),
        ), data = SortHabits.ByColor
    )
    val optionSortManually = SortOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.sort_manually),
            icon = IconResource.Id(R.drawable.sort_numbers),
        ), data = SortHabits.Manually
    )
    val optionFilterNone = FilterOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.filter_none),
            icon = IconResource.Id(R.drawable.filter_x),
        ), data = FilterHabits.None
    )
    val optionFilterByState = FilterOption(
        option = OptionUi(
            title = StringResource.ResId(R.string.filter_by_state),
            icon = IconResource.Id(R.drawable.filter_check),
        ), data = FilterHabits.ByState
    )

    val viewOptions: List<ViewOption> = listOf(optionCalendar, optionDaily)
    val sortOptions: List<SortOption> =
        listOf(optionSortByTitle, optionSortByColor, optionSortManually)
    val filterOptions: List<FilterOption> = listOf(optionFilterNone, optionFilterByState)
}