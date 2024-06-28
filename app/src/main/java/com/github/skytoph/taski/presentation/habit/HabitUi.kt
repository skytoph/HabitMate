package com.github.skytoph.taski.presentation.habit

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import java.util.Locale

@Stable
data class HabitUi(
    val id: Long = ID_DEFAULT,
    val title: String,
    val goal: Int = 1,
    val color: Color = IconsColors.Default,
    val icon: IconResource = IconResource.Default,
    val priority: Int = 0,
    val isArchived: Boolean = false,
    val frequency: FrequencyUi = FrequencyUi.Daily(),
    val reminder: ReminderUi = ReminderUi(),
) {

    fun map(mapper: HabitDomainMapper, context: Context) =
        mapper.map(
            id,
            title,
            goal,
            color,
            icon.name(context.resources),
            priority,
            isArchived,
            frequency
        )

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

data class ReminderUi(
    val switchedOn: Boolean = false,
    val hour: Int = 12,
    val minute: Int = 0,
    val isDialogShown: Boolean = false
) {
    fun formatted(locale: Locale): String {
        return String.format(locale, "%02d:%02d", hour, minute)
    }
}