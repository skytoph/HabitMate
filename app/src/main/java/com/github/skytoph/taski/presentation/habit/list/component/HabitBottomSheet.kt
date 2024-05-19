@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R

@Composable
fun HabitBottomSheet(
    hideBottomSheet: () -> Unit = {},
    deleteHabit: () -> Unit = {},
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ContextMenuItem(
                title = "edit",
                icon = Icons.Default.Edit,
                onClick = { hideBottomSheet(); editHabit() })
            Divider()
            ContextMenuItem(
                title = "delete",
                icon = Icons.Default.Delete,
                onClick = { deleteHabit() })
            Divider()
            ContextMenuItem(
                title = "reorder habits",
                icon = ImageVector.vectorResource(id = R.drawable.arrow_up_down),
                onClick = { hideBottomSheet(); reorder() })

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ContextMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = title)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}