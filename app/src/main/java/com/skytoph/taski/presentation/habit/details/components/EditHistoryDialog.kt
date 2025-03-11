package com.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.LazyPagingItems
import com.skytoph.taski.R
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi

@Composable
fun EditHistoryDialog(
    items: LazyPagingItems<EditableHistoryUi>,
    onEdit: () -> Unit,
    onDayClick: (Int) -> Unit,
    habitColor: Color,
    goal: Int,
    page: Int,
    isFirstDaySunday: Boolean = false,
    isCalendarView: Boolean = false,
) {
    Dialog(onDismissRequest = onEdit) {
        HabitHistoryEditable(items, habitColor, goal, page, onDayClick, isFirstDaySunday, isCalendarView)
    }
}

@Composable
fun HabitHistoryEditable(
    items: LazyPagingItems<EditableHistoryUi>,
    habitColor: Color,
    goal: Int,
    page: Int,
    onDayClick: (Int) -> Unit,
    isFirstDaySunday: Boolean = false,
    isCalendarView: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (isCalendarView) {
            MonthlyPager(
                goal = goal,
                habitColor = habitColor,
                isFirstDaySunday = isFirstDaySunday,
                isEditable = true,
                onEdit = onDayClick,
                items = items,
                pagerState = rememberPagerState(pageCount = { items.itemCount }, initialPage = page)
            )
        } else HabitHistoryGrid(
            items = items,
            habitColor = habitColor,
            goal = goal,
            isEditable = true,
            onDayClick = onDayClick,
            isFirstDaySunday = isFirstDaySunday
        )
        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.sparkles),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.edit_history_hint),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}