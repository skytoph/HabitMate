package com.github.skytoph.taski.presentation.habit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconRounded(imageVector: ImageVector, contentDescription: String? = null) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(30)
            )
            .size(24.dp)
    )
}