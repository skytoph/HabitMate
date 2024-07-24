package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithBackground(onClick: () -> Unit, text: String, background: Color, enabled: Boolean = true) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        .background(
            color = background,
            shape = MaterialTheme.shapes.small
        )
        .clip(MaterialTheme.shapes.small)
        .clickable(enabled = enabled) { onClick() }
        .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }
}