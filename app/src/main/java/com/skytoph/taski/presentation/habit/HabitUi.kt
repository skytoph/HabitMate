package com.skytoph.taski.presentation.habit

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.skytoph.taski.presentation.habit.icon.IconsColors
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

@Stable
data class HabitUi(
    val id: Long = ID_DEFAULT,
    val title: String,
    val description: String = "",
    val goal: Int = 1,
    val color: Color = IconsColors.Default,
    val icon: IconResource = IconResource.Default,
    val priority: Int = Int.MAX_VALUE,
    val isArchived: Boolean = false,
    val frequency: FrequencyUi = FrequencyUi.Daily(),
    val reminder: ReminderUi = ReminderUi(),
) {

    fun map(mapper: HabitDomainMapper, context: Context) = mapper.map(this, context)

    companion object {
        const val MIN_GOAL: Int = 1
        const val MAX_GOAL: Int = 30
        const val ID_DEFAULT: Long = -1L
    }
}

fun Color.applyColor(background: Color, alpha: Float): Color =
    this.copy(alpha = alpha).compositeOver(background)

interface HabitHistoryUi

interface HabitEntryUi

@Stable
data class HabitWithHistoryUi<T : HabitHistoryUi>(
    val habit: HabitUi,
    val history: T
)