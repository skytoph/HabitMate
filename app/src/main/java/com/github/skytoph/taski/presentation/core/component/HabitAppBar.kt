package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R

@Composable
fun HabitAppBar(
    modifier: Modifier = Modifier,
    label: String,
    navigateUp: () -> Unit,
    isSaveButtonVisible: Boolean = false,
    onSaveButtonClick: () -> Unit = {},
    isDeleteButtonVisible: Boolean = false,
    onDeleteButtonClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = navigateUp) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        if (isDeleteButtonVisible) {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.action_delete_habit),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        if (isSaveButtonVisible) {
            IconButton(onClick = {
                onSaveButtonClick()
                navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.action_save_habit),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}