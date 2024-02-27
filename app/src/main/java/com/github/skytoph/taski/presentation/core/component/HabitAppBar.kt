package com.github.skytoph.taski.presentation.core.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R

@Composable
fun HabitAppBar(
    modifier: Modifier = Modifier,
    label: String,
    navigateUp: () -> Unit,
    actionButtons: List<AppBarIcon> = emptyList()
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
        LazyRow {
            items(actionButtons, key = { it.title }) { button ->
                IconButton(onClick = button.action) {
                    Icon(
                        imageVector = button.icon,
                        contentDescription = stringResource(button.title),
                        modifier = Modifier.size(24.dp),
                        tint = button.color
                    )
                }
            }
        }
    }
}

abstract class AppBarIcon(
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val color: Color,
    val action: () -> Unit,
)

class EditIconButton(color: Color, action: () -> Unit) :
    AppBarIcon(R.string.edit_habit, Icons.Filled.Edit, color, action)

class DeleteIconButton(color: Color, action: () -> Unit) :
    AppBarIcon(R.string.action_delete, Icons.Filled.Close, color, action)

class SaveIconButton(color: Color, action: () -> Unit) :
    AppBarIcon(R.string.action_save_habit, Icons.Filled.Check, color, action)