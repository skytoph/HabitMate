package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.skytoph.taski.presentation.core.format.getWeekDisplayName

@Composable
fun WeekDayLabel(
    modifier: Modifier,
    index: Int,
    alignment: Alignment
) {
    Box(
        modifier = modifier,
        contentAlignment = alignment,
    ) {
        Text(
            text = getWeekDisplayName(getLocale(), index),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}