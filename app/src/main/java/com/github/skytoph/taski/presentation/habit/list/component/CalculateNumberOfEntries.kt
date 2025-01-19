package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.datastore.settings.ViewType

@Composable
@ReadOnlyComposable
fun calculateNumberOfEntries(maxWidth: Dp, view: ViewType): Int =
    if (view is ViewType.Daily) calculateNumberOfDailyEntries(maxWidth)
    else calculateNumberOfCalendarEntries(maxWidth)

@Composable
@ReadOnlyComposable
private fun calculateNumberOfEntries(
    maxWidth: Dp,
    entrySize: Dp,
    entriesSpacedBy: Dp,
    contentOffset: Dp,
): Int = maxWidth.minus(contentOffset).div(entrySize + entriesSpacedBy).toInt()

@Composable
@ReadOnlyComposable
fun calculateNumberOfDailyEntries(maxWidth: Dp): Int = calculateNumberOfEntries(
    maxWidth = maxWidth,
    entrySize = dimensionResource(R.dimen.entry_daily_size),
    entriesSpacedBy = dimensionResource(R.dimen.entries_daily_spaced_by),
    contentOffset = dimensionResource(R.dimen.habit_title_width)
)

@Composable
@ReadOnlyComposable
fun calculateNumberOfCalendarEntries(maxWidth: Dp): Int = calculateNumberOfEntries(
    maxWidth = maxWidth,
    entrySize = dimensionResource(R.dimen.entry_calendar_size),
    entriesSpacedBy = dimensionResource(R.dimen.entries_calendar_spaced_by),
    contentOffset = dimensionResource(R.dimen.entry_calendar_padding).times(2) +
            dimensionResource(R.dimen.entry_calendar_content_padding).times(2),
) + 2