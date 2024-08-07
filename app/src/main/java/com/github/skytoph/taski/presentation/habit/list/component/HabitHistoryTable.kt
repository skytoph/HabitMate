package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.color.borderColor
import com.github.skytoph.taski.presentation.core.leftFadingEdge
import com.github.skytoph.taski.presentation.core.preview.HabitProvider
import com.github.skytoph.taski.presentation.core.rightFadingEdge
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.applyColor
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlin.math.ceil

@Composable
fun HabitHistoryTable(
    modifier: Modifier = Modifier,
    habitColor: Color,
    history: List<EntryUi>,
) {
    val squareDp = dimensionResource(R.dimen.entry_calendar_size)
    val squareOffsetDp = dimensionResource(R.dimen.entries_calendar_spaced_by)
    val initialOffsetDp = dimensionResource(R.dimen.entry_calendar_padding)

    val state = rememberScrollState()
    LaunchedEffect(history.size) {
        state.scrollTo(state.maxValue)
    }

    val rows = 7
    val columns = ceil(history.size / 7.0)
    val height = rows * squareDp + 6 * squareOffsetDp
    val width = columns * squareDp + (columns - 1) * squareOffsetDp + 1.dp

    val defaultColor = MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10f)
            )
    ) {
        Box(
            modifier = Modifier
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
                    modifier = Modifier
                        .height(height)
                        .width(width)
                ) {
                    val squareSize = squareDp.toPx()
                    val squareOffset = squareOffsetDp.toPx()
                    val rectSize = Size(squareSize, squareSize)

                    history.forEachIndexed { index, entry ->
                        val stepX: Int = index / rows
                        val offsetX = stepX * squareSize + squareOffset * stepX
                        val stepY = index.mod(rows)
                        val offsetY = squareSize * stepY + squareOffset * stepY

                        drawRoundRect(
                            color = habitColor.applyColor(defaultColor, entry.percentDone),
                            cornerRadius = CornerRadius(5f, 5f),
                            style = Fill,
                            topLeft = Offset(offsetX, offsetY),
                            size = rectSize
                        )
                        if (entry.hasBorder) drawRoundRect(
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
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
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