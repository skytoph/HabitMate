package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun HabitAppBar(
    modifier: Modifier = Modifier,
    state: State<AppBarState>,
    navigateUp:() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.value.navigateUp.canNavigateUp)
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        Text(
            text = state.value.title.getString(context),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        if (state.value.dropdownItems.isNotEmpty())
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, "menu")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    state.value.dropdownItems.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item.title.getString(context),
                                    color = item.color
                                )
                            },
                            onClick = {
                                expanded = false
                                item.onClick()
                            })
                    }
                }
            }
        else
            LazyRow {
                items(state.value.menuItems, key = { it.title }) { button ->
                    IconButton(onClick = button.onClick) {
                        Icon(
                            imageVector = button.icon,
                            contentDescription = button.title.getString(context),
                            modifier = Modifier.size(24.dp),
                            tint = button.color
                        )
                    }
                }
            }
    }
}