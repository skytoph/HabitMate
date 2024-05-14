package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Segment
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.presentation.habit.list.view.OptionItem

@Composable
fun <T : Matches<T>> OptionsDropdown(
    title: String = "title",
    options: List<OptionItem<T>> = emptyList(),
    selected: OptionItem<T> = options.first(),
    selectOption: (OptionItem<T>) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Box {
            Row(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(selected.option.icon, "menu", Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selected.option.title.getString(LocalContext.current),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                options.forEach { option ->
                    if (!selected.item.matches(option.item))
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = option.option.title.getString(LocalContext.current),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Segment,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(24.dp)
                                )
                            },
                            onClick = {
                                expanded = false
                                selectOption(option)
                            })
                }
            }
        }
    }
}