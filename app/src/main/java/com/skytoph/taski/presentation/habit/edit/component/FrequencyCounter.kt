package com.skytoph.taski.presentation.habit.edit.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FrequencyCounter(
    count: Int,
    size: Dp = 32.dp
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraSmall)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                if (targetState > initialState)
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                else
                    slideInVertically { it } togetherWith slideOutVertically { -it }
            },
            label = "counter_anim"
        ) { count ->
            Text(
                text = count.toString(),
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}