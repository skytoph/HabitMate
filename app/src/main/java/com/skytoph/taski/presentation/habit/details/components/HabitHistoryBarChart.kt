package com.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.component.OptionsDropdown
import com.skytoph.taski.presentation.core.component.getLocale
import com.skytoph.taski.presentation.core.preview.BarChartEntryProvider
import com.skytoph.taski.presentation.habit.details.BarItemUi
import com.skytoph.taski.presentation.habit.details.ChartFrequencyUi
import com.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.min

@Composable
fun HabitHistoryBarChart(
    items: Flow<PagingData<BarItemUi>>,
    maxValue: Int = 0,
    color: Color = Color.Blue,
    frequency: ChartFrequencyUi = ChartFrequencyUi.Year,
    isTypeExpanded: Boolean = false,
    expandType: () -> Unit = {},
    selectFrequency: (ChartFrequencyUi) -> Unit = {},
) {
    BarChart(
        items = items.collectAsLazyPagingItems(),
        maxValue = maxValue,
        color = color,
        frequency = frequency,
        isTypeExpanded = isTypeExpanded,
        expandType = expandType,
        selectFrequency = selectFrequency
    )
}

@Composable
fun BarChart(
    items: LazyPagingItems<BarItemUi>,
    maxValue: Int = 0,
    color: Color = Color.Blue,
    frequency: ChartFrequencyUi = ChartFrequencyUi.Day,
    isTypeExpanded: Boolean = false,
    expandType: () -> Unit = {},
    selectFrequency: (ChartFrequencyUi) -> Unit = {},
    maxHeight: Dp = 180.dp,
    barWidth: Dp = 40.dp,
    labelTextStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            )
            .fillMaxWidth()
            .semantics {
                contentDescription = context.getString(R.string.content_description_history_bar_chart)
            },
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            OptionsDropdown(
                modifier = Modifier
                    .widthIn(min = 88.dp)
                    .semantics {
                        contentDescription =
                            context.getString(R.string.content_description_chart_frequency_dropdown)
                    },
                options = ChartFrequencyUi.options,
                selected = frequency,
                selectOption = selectFrequency,
                isExpanded = isTypeExpanded,
                expand = expandType,
                isExpandIconShown = true,
                itemsArrangement = Arrangement.SpaceBetween
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(8.dp),
        ) {
            Column {
                Text(
                    text = "",
                    style = labelTextStyle,
                )
                Column(
                    modifier = Modifier.height(maxHeight),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(6) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                reverseLayout = true,
                verticalAlignment = Alignment.Bottom
            ) {
                items(count = items.itemCount) { index ->
                    items[index]?.let { bar ->
                        BarItem(
                            item = bar,
                            maxValue = maxValue,
                            color = color,
                            maxHeight = maxHeight,
                            barWidth = barWidth,
                            labelTextStyle = labelTextStyle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BarItem(
    item: BarItemUi,
    maxValue: Int = 0,
    color: Color,
    maxHeight: Dp = 180.dp,
    barWidth: Dp = 40.dp,
    labelTextStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    val animatablePercentDone: Animatable<Float, AnimationVector1D> =
        remember { Animatable(initialValue = min(item.percentDone(maxValue), 1f)) }
    val context = LocalContext.current
    LaunchedEffect(item.timesDone, maxValue) {
        animatablePercentDone.animateTo(
            targetValue = min(item.percentDone(maxValue), 1f),
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }
    val displayDate = item.getDisplayDate(getLocale(), context)
    Column(
        modifier = Modifier
            .width(barWidth)
            .padding(horizontal = 4.dp)
            .testTag(stringResource(R.string.test_tag_bar_chart_item, displayDate, item.timesDone)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.height(maxHeight - maxHeight.times(animatablePercentDone.value)))
        Text(
            text = item.timesDone.let { if (it == 0) "" else it.toString() },
            color = MaterialTheme.colorScheme.onBackground,
            style = labelTextStyle,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(maxHeight.times(animatablePercentDone.value))
                .heightIn(max = maxHeight)
                .background(color = color, shape = MaterialTheme.shapes.extraSmall)
        )
        AnimatedVisibility(visible = !item.isEmpty) {
            Text(
                text = displayDate,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                minLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible
            )
        }
    }
}

@Composable
@Preview(locale = "uk")
private fun HabitHistoryBarChartPreview(
    @PreviewParameter(BarChartEntryProvider::class) entries: List<BarItemUi>,
) {
    HabitMateTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            HabitHistoryBarChart(items = flowOf(PagingData.from(entries)), maxValue = 5, isTypeExpanded = true)
        }
    }
}

