package com.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.skytoph.taski.presentation.core.format.getWeekDisplayName
import java.util.Locale

@Composable
fun WeekDayLabel(
    modifier: Modifier,
    index: Int,
    alignment: Alignment = Alignment.CenterStart,
    locale: Locale = getLocale(),
    color: Color = MaterialTheme.colorScheme.onBackground,
) {
    Box(
        modifier = modifier,
        contentAlignment = alignment,
    ) {
        Text(
            text = getWeekDisplayName(locale, index),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}