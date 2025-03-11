package com.skytoph.taski.presentation.habit.list.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

object HabitsViewTypesProvider {
    val optionCalendar = OptionUi(
        title = StringResource.ResId(R.string.option_calendar),
        icon = IconResource.Id(R.drawable.rows_2),
    )
    val optionDaily = OptionUi(
        title = StringResource.ResId(R.string.option_daily),
        icon = IconResource.Id(R.drawable.rows_3),
    )
    val optionSortByTitle = OptionUi(
        title = StringResource.ResId(R.string.sort_by_title),
        icon = IconResource.Id(R.drawable.sort_characters),
    )
    val optionSortByColor = OptionUi(
        title = StringResource.ResId(R.string.sort_by_color),
        icon = IconResource.Id(R.drawable.sort_color),
    )
    val optionSortByState = OptionUi(
        title = StringResource.ResId(R.string.sort_by_state),
        icon = IconResource.Id(R.drawable.sort_state),
    )
    val optionSortManually = OptionUi(
        title = StringResource.ResId(R.string.sort_manually),
        icon = IconResource.Id(R.drawable.sort_numbers),
    )
    val optionFilterNone = OptionUi(
        title = StringResource.ResId(R.string.filter_none),
        icon = IconResource.Id(R.drawable.filter_x),
    )
    val optionFilterByState = OptionUi(
        title = StringResource.ResId(R.string.filter_by_state),
        icon = IconResource.Id(R.drawable.filter_check),
    )
    val optionFilterArchived = OptionUi(
        title = StringResource.ResId(R.string.filter_archived),
        icon = IconResource.Id(R.drawable.archive),
    )
    val optionFilterToday = OptionUi(
        title = StringResource.ResId(R.string.show_only_todays_habits),
        icon = IconResource.Vector(Icons.Default.Check)
    )
}