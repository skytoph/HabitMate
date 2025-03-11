package com.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skytoph.taski.presentation.habit.edit.StreakType
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun StreakLine(type: StreakType, squareSize: Dp = 40.dp, radius: Dp = 10.dp, color: Color) {
    Box {
        Canvas(modifier = Modifier.size(squareSize),
            onDraw = {
                when (type) {
                    StreakType.Dot -> {
                        drawCircle(
                            color = color,
                            radius = radius.toPx(),
                        )
                    }

                    StreakType.End -> clipRect {
                        drawLine(
                            color = color,
                            start = center,
                            end = center.copy(x = size.width),
                            strokeWidth = radius.times(2).toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    StreakType.Middle -> clipRect {
                        drawLine(
                            color = color,
                            start = center.copy(x = 0f),
                            end = center.copy(x = size.width.plus(3f)),
                            strokeWidth = radius.times(2).toPx(),
                        )
                    }

                    StreakType.Start -> clipRect {
                        drawLine(
                            color = color,
                            start = center.copy(x = 0f),
                            end = center,
                            strokeWidth = radius.times(2).toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            })
    }
}

@Preview
@Composable
private fun StreakDotPreview() {
    HabitMateTheme {
        Row {
            StreakLine(type = StreakType.Dot, color = Color.Red)
            StreakLine(type = StreakType.Start, color = Color.Red)
            StreakLine(type = StreakType.Middle, color = Color.Red)
            StreakLine(type = StreakType.End, color = Color.Red)
        }
    }
}