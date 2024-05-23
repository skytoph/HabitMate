@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.TitleWithIconMenuItem

@Composable
fun HabitBottomSheet(
    hideBottomSheet: () -> Unit = {},
    deleteHabit: () -> Unit = {},
    archiveHabit: () -> Unit,
    editHabit: () -> Unit = {},
    reorder: () -> Unit = {},
) {
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        sheetState = state
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            TitleWithIconMenuItem(
                title = "edit",
                icon = ImageVector.vectorResource(id = R.drawable.pencil),
                onClick = { hideBottomSheet(); editHabit() })
            Divider()
            TitleWithIconMenuItem(
                title = "archive",
                icon = ImageVector.vectorResource(id = R.drawable.archive_down),
                onClick = { archiveHabit() })
            Divider()
            TitleWithIconMenuItem(
                title = "delete",
                icon = ImageVector.vectorResource(id = R.drawable.trash),
                onClick = { deleteHabit() })
            Divider()
            TitleWithIconMenuItem(
                title = "reorder habits",
                icon = ImageVector.vectorResource(id = R.drawable.list),
                onClick = { hideBottomSheet(); reorder() })

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}