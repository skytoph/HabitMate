@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
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
import com.github.skytoph.taski.presentation.habit.list.component.ViewBottomSheet
import com.github.skytoph.taski.presentation.habit.list.view.ProvideOptionUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun <T : ProvideOptionUi<T>> OptionsDropdown(
    modifier: Modifier = Modifier,
    title: String = "title",
    options: List<T> = emptyList(),
    selected: T = options.first(),
    selectOption: (T) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Box(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val option = selected.optionUi()
                Icon(
                    imageVector = option.icon.vector(context),
                    contentDescription = option.icon.name(context.resources),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = option.title.getString(context),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
            ) {
                options.forEach { option ->
                    if (!selected.matches(option)) {
                        val optionUi = option.optionUi()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = optionUi.title.getString(context),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = optionUi.icon.vector(context),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(24.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
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
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun BottomSheetPreview() {
    HabitMateTheme(darkTheme = true) {
        ViewBottomSheet(state = rememberStandardBottomSheetState())
    }
}