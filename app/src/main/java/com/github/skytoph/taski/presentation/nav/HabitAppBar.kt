package com.github.skytoph.taski.presentation.nav

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.component.AppBarState

@Composable
fun HabitAppBar(
    modifier: Modifier = Modifier,
    state: State<AppBarState>,
    navigateUp: () -> Unit,
    expandList: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Crossfade(
        targetState = state.value,
        label = "app_bar_crossfade",
        animationSpec = tween(60)
    ) { state ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.navigateUp.canNavigateUp)
                IconButton(onClick = navigateUp) {
                    state.navigateUp.action.icon?.let { icon ->
                        Icon(
                            imageVector = icon.vector(context),
                            contentDescription = state.navigateUp.action.title.getString(context),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            else
                Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = state.title.getString(context),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                items(state.menuItems, key = { it.title }) { button ->
                    IconButton(onClick = button.onClick) {
                        val description = button.title.getString(context)
                        button.icon?.let { icon ->
                            Icon(
                                imageVector = icon.vector(context),
                                contentDescription = description,
                                modifier = Modifier
                                    .size(24.dp)
                                    .semantics { contentDescription = description },
                                tint = button.color
                            )
                        }
                    }
                }
            }
            if (state.dropdownItems.isNotEmpty())
                Box {
                    IconButton(onClick = { expandList(true) }) {
                        Icon(Icons.Default.MoreVert, "menu")
                    }

                    DropdownMenu(
                        expanded = state.isListExpanded,
                        onDismissRequest = { expandList(false) },
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        state.dropdownItems.forEach { item ->
                            val title = item.title.getString(context)
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        item.checked?.let { checked ->
                                            Checkbox(checked = checked,
                                                onCheckedChange = {
                                                    expandList(false)
                                                    item.onClick()
                                                })
                                        }
                                        Text(
                                            text = title,
                                            color = item.color,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                },
                                leadingIcon = item.icon?.let { icon ->
                                    {
                                        Icon(
                                            imageVector = icon.vector(context),
                                            contentDescription = title,
                                            tint = item.color,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                onClick = {
                                    expandList(false)
                                    item.onClick()
                                })
                        }
                    }
                }
        }
    }
}