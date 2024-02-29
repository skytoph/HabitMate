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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.skytoph.taski.presentation.core.borderColor
import com.github.skytoph.taski.presentation.core.habitColor
import com.github.skytoph.taski.presentation.core.leftFadingEdge
import com.github.skytoph.taski.presentation.core.preview.HabitProvider
import com.github.skytoph.taski.presentation.core.rightFadingEdge
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.TaskiTheme
import kotlin.math.ceil

@Composable
fun HabitCalendar(
    modifier: Modifier = Modifier,
    habitColor: Color,
    history: HistoryUi,
) {
    val squareDp = 10.dp
    val squareOffsetDp = 2.dp
    val initialOffsetDp = squareOffsetDp.times(2)

    val state = rememberScrollState()
    LaunchedEffect(key1 = Unit) {
        state.scrollTo(state.maxValue)
    }

    val rows = 7
    val columns = ceil(history.entries.size / 7.0)
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

                    history.entries.forEachIndexed { index, entry ->
                        val stepX: Int = index / rows
                        val offsetX = stepX * squareSize + squareOffset * stepX
                        val stepY = index.mod(rows)
                        val offsetY = squareSize * stepY + squareOffset * stepY

                        drawRoundRect(
                            color = habitColor(entry.colorPercent, defaultColor, habitColor),
                            cornerRadius = CornerRadius(5f, 5f),
                            style = Fill,
                            topLeft = Offset(offsetX, offsetY),
                            size = rectSize
                        )
                        if (index == history.todayPosition) drawRoundRect(
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
fun HabitCalendarPreview(@PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>) {
    TaskiTheme {
        HabitCard(onDone = {}, habit = habit.habit, history = habit.history)
    }
}