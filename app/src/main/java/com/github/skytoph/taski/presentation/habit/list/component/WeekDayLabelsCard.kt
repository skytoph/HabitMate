package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.MonthDayLabel
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.format.getDayOfWeek
import java.util.Locale

@Composable
fun WeekDayLabelsCard(
    modifier: Modifier = Modifier,
    entries: Int = 5,
    locale: Locale = getLocale(),
) {
    Card(
        modifier = modifier
            .widthIn(max = 520.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.entries_daily_spaced_by))) {
                for (index in 0 until entries) {
                    Column {
                        WeekDayLabel(Modifier.width(48.dp), getDayOfWeek(locale, index), Alignment.Center)
                        MonthDayLabel(Modifier.width(48.dp), -index, TextAlign.Center)
                    }
                }
            }
        }
    }
}