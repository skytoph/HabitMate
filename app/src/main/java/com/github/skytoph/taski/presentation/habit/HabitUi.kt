package com.github.skytoph.taski.presentation.habit

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

@Stable
data class HabitUi(
    val id: Long = ID_DEFAULT,
    val title: String,
    val goal: Int = 1,
    val color: Color = IconsColors.Default,
    val icon: IconResource = IconResource.Default,
) {

    fun map(mapper: HabitDomainMapper, context: Context) =
        mapper.map(id, title, goal, color, icon.name(context.resources))

    companion object {
        const val MIN_GOAL: Int = 1
        const val MAX_GOAL: Int = 30
        const val ID_DEFAULT: Long = -1L
    }
}

interface HabitHistoryUi

interface HabitEntryUi

@Stable
data class HabitWithHistoryUi<T : HabitHistoryUi>(
    val habit: HabitUi,
    val history: T
)