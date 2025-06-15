package com.skytoph.taski.presentation.habit.list.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.color.borderColor
import com.skytoph.taski.presentation.core.leftFadingEdge
import com.skytoph.taski.presentation.core.preview.HabitProvider
import com.skytoph.taski.presentation.core.rightFadingEdge
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.applyColor
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun HabitHistoryTable(
    modifier: Modifier = Modifier,
    habitColor: Color,
    history: HistoryUi,
    columns: Int,
    rows: Int = 7,
    squareDp: Dp = dimensionResource(R.dimen.entry_calendar_size),
    squareOffsetDp: Dp = dimensionResource(R.dimen.entries_calendar_spaced_by),
    initialOffsetDp: Dp = dimensionResource(R.dimen.entry_calendar_padding),
    height: Dp = rows * squareDp + 6 * squareOffsetDp,
    width: Dp = columns * squareDp + (columns - 1) * squareOffsetDp + 1.dp,
    defaultColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    val state = rememberScrollState()
    LaunchedEffect(columns) {
        state.scrollTo(state.maxValue)
    }

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10f)
            )
            .height(height + initialOffsetDp * 2)
            .fillMaxWidth()
            .padding(initialOffsetDp)
            .leftFadingEdge(
                color = MaterialTheme.colorScheme.secondaryContainer,
                width = 4.dp,
                spec = tween(100),
                isVisible = state.canScrollBackward,
            )
            .rightFadingEdge(
                color = MaterialTheme.colorScheme.secondaryContainer,
                width = 4.dp,
                spec = tween(100),
                isVisible = state.canScrollForward
            )
    ) {
        Box(
            modifier = Modifier.horizontalScroll(state)
        ) {
            Canvas(
                modifier = Modifier.size(width = width, height = height)
            ) {
                val squareSize = squareDp.toPx()
                val squareOffset = squareOffsetDp.toPx()
                val rectSize = Size(squareSize, squareSize)

                (history.toDaysAgo downTo history.fromDaysAgo).forEach { daysAgo ->
                    val entry = history.entries[daysAgo]
                    val index = history.toDaysAgo - daysAgo
                    val stepX: Int = index / rows
                    val offsetX = stepX * squareSize + squareOffset * stepX
                    val stepY = index.mod(rows)
                    val offsetY = squareSize * stepY + squareOffset * stepY

                    drawRoundRect(
                        color = habitColor.applyColor(defaultColor, entry?.percentDone ?: 0f),
                        cornerRadius = CornerRadius(5f, 5f),
                        style = Fill,
                        topLeft = Offset(offsetX, offsetY),
                        size = rectSize
                    )
                    if (entry?.hasBorder == true) drawRoundRect(
                        color = borderColor(habitColor),
                        cornerRadius = CornerRadius(5f, 5f),
                        style = Stroke(1.dp.toPx()),
                        topLeft = Offset(offsetX, offsetY),
                        size = rectSize
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun HabitCalendarPreview(
    @PreviewParameter(
        HabitProvider::class,
        limit = 1
    ) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme(darkTheme = true) {
        HabitCalendar(
            onDone = {},
            habit = habit.habit,
            history = habit.history,
        )
    }
}