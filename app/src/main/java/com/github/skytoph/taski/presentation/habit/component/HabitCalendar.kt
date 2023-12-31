package com.github.skytoph.taski.presentation.habit.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.skytoph.taski.presentation.habit.HabitUi
import kotlin.math.ceil

@Composable
fun HabitCalendar(
    modifier: Modifier = Modifier,
    habit: HabitUi,
    numberOfDays: Int = HabitUi.MAX_DAYS
) {
    val items = (1..numberOfDays).toList()

    val squareDp = 10.dp
    val squareOffsetDp = squareDp / 5
    val initialOffsetDp = squareOffsetDp * 2

    val state = rememberScrollState()
    LaunchedEffect(key1 = Unit) {
        state.scrollTo(state.maxValue)
    }

    val rows = 7
    val columns = ceil(numberOfDays / 7.0)
    val height = rows * squareDp + 6 * squareOffsetDp + 2 * initialOffsetDp
    val width = columns * squareDp + (columns - 1) * squareOffsetDp + 2 * initialOffsetDp
    Box {
        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10f)
                )
                .height(height)
                .fillMaxWidth()
        )
        val colorOnSurface = MaterialTheme.colorScheme.onSurface
        Box(
            modifier = modifier.horizontalScroll(state)
        ) {
            Canvas(
                modifier = Modifier
                    .height(height)
                    .width(width)
            ) {
                val squareSize = squareDp.toPx()
                val initialOffset = initialOffsetDp.toPx()
                val squareOffset = squareOffsetDp.toPx()

                items.forEachIndexed { index, item ->
                    val stepX: Int = index / rows
                    val offsetX = initialOffset + stepX * squareSize + squareOffset * stepX
                    val stepY = index.mod(rows)
                    val offsetY = initialOffset + squareSize * stepY + squareOffset * stepY
                    drawRoundRect(
                        color = if (habit.history.contains(numberOfDays - index)) habit.color else colorOnSurface,
                        cornerRadius = CornerRadius(5f, 5f),
                        style = Fill,
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(squareSize, squareSize)
                    )
                    if (index == habit.todayPositions) drawRoundRect(
                        color = Color.Black,
                        cornerRadius = CornerRadius(5f, 5f),
                        style = Stroke(1.dp.toPx()),
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(squareSize, squareSize)
                    )
                }
            }
        }
    }
}