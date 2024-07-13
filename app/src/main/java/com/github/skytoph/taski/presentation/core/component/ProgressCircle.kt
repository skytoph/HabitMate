package com.github.skytoph.taski.presentation.core.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ProgressCircle(goal: Int, done: Int, size: Dp = 50.dp, width: Dp = 3.dp, padding: Dp = 0.dp, color: Color) {
    val angleState = remember { mutableStateOf(0f) }
    val list: List<Animatable<Float, AnimationVector1D>> = remember { MutableList(goal) { Animatable(0f) } }
    LaunchedEffect(done) {
        list.forEachIndexed { index, item ->
            if (index == done - 1 && item.value != 0f) item.apply {
                snapTo(0f)
                animateTo(
                    targetValue = angleState.value,
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )
            }
            else item.snapTo(angleState.value)
        }
    }
    Box {
        Canvas(modifier = Modifier
            .size(size)
            .padding(width.div(2) + padding),
            onDraw = {
                val offset = width.times(3).toPx()
                var startAngle = 180f + width.toPx().times(1.5F)
                val sweepAngle: Float = 360F / goal - offset

                repeat(goal) { index ->

                    drawArc(
                        color = color.copy(alpha = 0.2f),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(
                            width = width.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    startAngle += sweepAngle + width.times(3).toPx()
                }

                startAngle = 180f + width.toPx().times(1.5F)

                repeat(done) { index ->

                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = list[index].value,
                        useCenter = false,
                        style = Stroke(
                            width = width.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    startAngle += sweepAngle + width.times(3).toPx()
                    angleState.value = sweepAngle
                }
            })
    }
}

@Preview
@Composable
private fun ProgressPreview() {
    HabitMateTheme {
        ProgressCircle(goal = 4, done = 3, color = Color.Red)
    }
}