package com.github.skytoph.taski.presentation.habit.list.view

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.presentation.core.state.StringResource

@Stable
abstract class OptionItem<T : Matches<T>>(
    open val option: Option,
    open val item: T
)

@Stable
data class Option(
    val icon: ImageVector,
    val title: StringResource,
)


@Stable
data class ViewOption(
    override val option: Option,
    override val item: ViewType
) : OptionItem<ViewType>(option, item)

@Stable
data class SortOption(
    override val option: Option,
    override val item: SortHabits
) : OptionItem<SortHabits>(option, item)

@Stable
data class FilterOption(
    override val option: Option,
    override val item: FilterHabits
) : OptionItem<FilterHabits>(option, item)
