@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.component.TitleWithIconMenuItem
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun HabitBottomSheet(
    state: SheetState = rememberModalBottomSheetState(),
    hideBottomSheet: () -> Unit = {},
    deleteHabit: () -> Unit = {},
    archiveHabit: () -> Unit = {},
    editHabit: () -> Unit = {},
    reorder: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            TitleWithIconMenuItem(
                title = stringResource(R.string.habit_menu_edit),
                icon = ImageVector.vectorResource(id = R.drawable.pencil),
                onClick = { hideBottomSheet(); editHabit() })
            HorizontalDivider()
            TitleWithIconMenuItem(
                title = stringResource(R.string.habit_menu_archive),
                icon = ImageVector.vectorResource(id = R.drawable.archive_down),
                onClick = { archiveHabit() })
            HorizontalDivider()
            TitleWithIconMenuItem(
                title = stringResource(R.string.habit_menu_delete),
                icon = ImageVector.vectorResource(id = R.drawable.trash),
                onClick = { deleteHabit() })
            HorizontalDivider()
            TitleWithIconMenuItem(
                title = stringResource(R.string.habit_menu_reorder_habits),
                icon = ImageVector.vectorResource(id = R.drawable.arrow_up_down),
                onClick = { hideBottomSheet(); reorder() })

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
@Preview
private fun HabitBottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        HabitBottomSheet(state = rememberStandardBottomSheetState())
    }
}