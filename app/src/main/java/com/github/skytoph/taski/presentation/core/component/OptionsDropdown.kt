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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.presentation.habit.list.component.ViewBottomSheet
import com.github.skytoph.taski.presentation.habit.list.view.OptionItem
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun <M : Matches<M>, T : OptionItem<M>> OptionsDropdown(
    title: String = "title",
    options: List<T> = emptyList(),
    selected: T = options.first(),
    selectOption: (T) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            style = MaterialTheme.typography.titleMedium
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
                                Text(
                                    text = option.option.title.getString(LocalContext.current),
                                    style = MaterialTheme.typography.titleSmall
                                )
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

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun BottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        ViewBottomSheet()
    }
}