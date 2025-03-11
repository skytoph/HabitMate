package com.skytoph.taski.presentation.core.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f,
): EnterTransition = scaleIn(
        animationSpec = tween(durationMillis = 220, delayMillis = 60), initialScale = initialScale
    ) + fadeIn(tween(durationMillis = 180, delayMillis = 60))

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition = scaleOut(
        animationSpec = tween(durationMillis = 180, delayMillis = 60), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 60))