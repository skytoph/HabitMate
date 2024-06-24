package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.PagingData
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import kotlinx.coroutines.flow.Flow

@Composable
fun EditHistoryDialog(
    entries: Flow<PagingData<EditableHistoryUi>>,
    onEdit: () -> Unit,
    onDayClick: (Int) -> Unit,
    habitColor: Color,
    goal: Int
) {
    Dialog(onDismissRequest = onEdit) {
        HabitHistoryGridEditable(entries, habitColor, goal, onDayClick)
    }
}

@Composable
fun HabitHistoryGridEditable(
    entries: Flow<PagingData<EditableHistoryUi>>,
    habitColor: Color,
    goal: Int,
    onDayClick: (Int) -> Unit
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
    ) {
        HabitHistoryGrid(
            entries = entries,
            habitColor = habitColor,
            goal = goal,
            isEditable = true,
            onDayClick = onDayClick,
        )
        Text(
            text = stringResource(R.string.edit_history_hint),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}