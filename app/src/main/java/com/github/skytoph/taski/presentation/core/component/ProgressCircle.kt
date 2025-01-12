package com.github.skytoph.taski.presentation.core.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    val angleState = remember { mutableFloatStateOf(0f) }
    val angleAnimatable: Animatable<Float, AnimationVector1D> = remember { Animatable(0f) }
    val alphaAnimatable: Animatable<Float, AnimationVector1D> = remember { Animatable(0f) }
    LaunchedEffect(done) {
        angleAnimatable.snapTo(0f)
        angleAnimatable.animateTo(
            targetValue = angleState.value,
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
        )
        alphaAnimatable.snapTo(0f)
        if (done >= goal)
            alphaAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
            )
    }
    Box {
        Canvas(modifier = Modifier
            .size(size)
            .padding(width.div(2) + padding),
            onDraw = {
                val offset = width.times(3).toPx()
                var startAngle = 180f + width.toPx().times(1.5F)
                val sweepAngle: Float = 360F / goal - offset
                angleState.value = sweepAngle

                if(done > 0) repeat(goal) {

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

                repeat(done - 1) {

                    drawArc(
                        color = color,
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
                if (done > 0) drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = angleAnimatable.value,
                    useCenter = false,
                    style = Stroke(
                        width = width.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                if (done >= goal) drawCircle(
                    color = color,
                    style = Stroke(
                        width = width.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    alpha = alphaAnimatable.value
                )
            })
    }
}

@Preview
@Composable
private fun ProgressPreview() {
    HabitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ProgressCircle(goal = 4, done = 1, color = Color.Red)
            ProgressCircle(goal = 4, done = 2, color = Color.Red)
            ProgressCircle(goal = 4, done = 3, color = Color.Red)
            ProgressCircle(goal = 4, done = 4, color = Color.Red)
            ProgressCircle(goal = 4, done = 5, color = Color.Red)
        }
    }
}

@Preview
@Composable
private fun ProgressClickablePreview() {
    HabitMateTheme {
        val goal = 4
        val state = remember { mutableIntStateOf(0) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.clickable { state.value = if (state.value < goal) state.value + 1 else 0 }) {
                ProgressCircle(goal = goal, done = state.value, color = Color.Red)
            }
        }
    }
}