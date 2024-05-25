package com.github.skytoph.taski.presentation.core.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.skytoph.taski.presentation.core.format.getTodayDayOfMonth
import java.util.Locale

@Composable
fun MonthDayLabel(
    modifier: Modifier,
    index: Int,
    alignment: TextAlign,
    locale: Locale = getLocale()
) {
    Text(
        text = getTodayDayOfMonth(locale, index).toString(),
        style = MaterialTheme.typography.labelSmall,
        fontSize = 9.sp,
        textAlign = alignment,
        modifier = modifier
    )
}