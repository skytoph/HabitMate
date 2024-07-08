package com.github.skytoph.taski.presentation.habit.list.view

import androidx.compose.runtime.Stable
import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

@Stable
interface OptionItem<T : Matches<T>> {
    val option: OptionUi
    val data: T
}

@Stable
data class OptionUi(
    val icon: IconResource,
    val title: StringResource,
)

@Stable
data class ViewOption(
    override val option: OptionUi,
    override val data: ViewType
) : OptionItem<ViewType>

@Stable
data class SortOption(
    override val option: OptionUi,
    override val data: SortHabits
) : OptionItem<SortHabits>

@Stable
data class FilterOption(
    override val option: OptionUi,
    override val data: FilterHabits
) : OptionItem<FilterHabits>
