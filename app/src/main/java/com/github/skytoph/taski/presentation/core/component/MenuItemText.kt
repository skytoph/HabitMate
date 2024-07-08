package com.github.skytoph.taski.presentation.core.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun MenuItemText(modifier: Modifier = Modifier, text: String, style: TextStyle = MaterialTheme.typography.bodySmall) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
fun MenuTitleText(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}