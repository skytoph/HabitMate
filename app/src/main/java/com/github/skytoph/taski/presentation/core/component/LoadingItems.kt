package com.github.skytoph.taski.presentation.core.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.delay


@Composable
fun LoadingFullscreen(
    modifier: Modifier = Modifier,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    circleSize: Dp = 16.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(),
        contentAlignment = Alignment.Center
    ) {
        LoadingItems(
            itemColor = circleColor,
            itemSize = circleSize,
            animationDelay = animationDelay,
            initialAlpha = initialAlpha,
            item = ImageVector.vectorResource(id = R.drawable.sparkle_filled)
        )
    }
}

@Composable
fun LoadingItems(
    itemColor: Color = MaterialTheme.colorScheme.primary,
    itemSize: Dp = 16.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f,
    item: ImageVector = ImageVector.vectorResource(id = R.drawable.sparkle_filled)
) {
    val animatableList = listOf(
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        }
    )

    animatableList.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay(timeMillis = (animationDelay / animatableList.size).toLong() * index)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = animationDelay),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Row {
        animatableList.forEachIndexed { index, animatable ->
            if (index != 0)
                Spacer(modifier = Modifier.width(width = 8.dp))
            Box(modifier = Modifier.size(itemSize)) {
                Icon(
                    imageVector = item,
                    contentDescription = null,
                    tint = itemColor.copy(alpha = animatable.value)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CirclesAnimationPreview() {
    HabitMateTheme(darkTheme = true) {
        LoadingFullscreen()
    }
}